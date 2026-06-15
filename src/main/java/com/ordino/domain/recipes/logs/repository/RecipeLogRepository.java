package com.ordino.domain.recipes.logs.repository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

@Repository
public class RecipeLogRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public List<Tuple> findPagedLogEntries(Long recipeId, int pageSize, int offset, Instant from, Instant to) {
        String dateCondition = buildDateCondition(from, to);

        String sql = """
                SELECT 'EDIT' AS type, id, created_at FROM recipe_edit_logs
                WHERE recipe_id = :recipeId%s
                UNION ALL
                SELECT 'REVIEW' AS type, id, created_at FROM recipe_review_logs
                WHERE recipe_id = :recipeId%s
                UNION ALL
                SELECT 'ARCHIVE' AS type, id, created_at FROM recipe_archive_logs
                WHERE recipe_id = :recipeId%s
                ORDER BY created_at DESC
                """.formatted(dateCondition, dateCondition, dateCondition);

        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("recipeId", recipeId);
        if (from != null) query.setParameter("from", Timestamp.from(from));
        if (to != null) query.setParameter("to", Timestamp.from(to));
        query.setFirstResult(offset);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    public long countLogEntries(Long recipeId, Instant from, Instant to) {
        String dateCondition = buildDateCondition(from, to);

        String sql = """
                SELECT COUNT(*) FROM (
                    SELECT id FROM recipe_edit_logs WHERE recipe_id = :recipeId%s
                    UNION ALL
                    SELECT id FROM recipe_review_logs WHERE recipe_id = :recipeId%s
                    UNION ALL
                    SELECT id FROM recipe_archive_logs WHERE recipe_id = :recipeId%s
                ) AS all_logs
                """.formatted(dateCondition, dateCondition, dateCondition);

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("recipeId", recipeId);
        if (from != null) query.setParameter("from", Timestamp.from(from));
        if (to != null) query.setParameter("to", Timestamp.from(to));

        return ((Number) query.getSingleResult()).longValue();
    }

    private String buildDateCondition(Instant from, Instant to) {
        StringBuilder sb = new StringBuilder();
        if (from != null) sb.append(" AND created_at >= :from");
        if (to != null) sb.append(" AND created_at <= :to");
        return sb.toString();
    }
}
