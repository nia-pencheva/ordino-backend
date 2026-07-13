package com.ordino.domain.recipes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ordino.domain.recipes.model.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findByRecipeStatusId(Long recipeStatusId, Pageable pageable);

    Page<Recipe> findByRecipeStatusIdAndCreatedById(Long recipeStatusId, Long createdById, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT *, 1 AS search_rank FROM recipes WHERE LOWER(title) = LOWER(:title) AND recipe_status_id = :statusId
                UNION
                SELECT *, 2 AS search_rank FROM recipes WHERE LOWER(title) LIKE CONCAT(LOWER(:title), '%') AND recipe_status_id = :statusId
                UNION
                SELECT *, 3 AS search_rank FROM recipes WHERE LOWER(title) LIKE CONCAT('%', LOWER(:title), '%') AND recipe_status_id = :statusId
            ) t
            GROUP BY id
            ORDER BY search_rank, title
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes
            WHERE LOWER(title) LIKE CONCAT('%', LOWER(:title), '%')
            AND recipe_status_id = :statusId
        """,
        nativeQuery = true
    )
    Page<Recipe> searchByTitleAndStatusId(@Param("title") String title, @Param("statusId") Long statusId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT *, 1 AS search_rank FROM recipes WHERE LOWER(title) = LOWER(:title) AND recipe_status_id = :statusId AND created_by = :createdById
                UNION
                SELECT *, 2 AS search_rank FROM recipes WHERE LOWER(title) LIKE CONCAT(LOWER(:title), '%') AND recipe_status_id = :statusId AND created_by = :createdById
                UNION
                SELECT *, 3 AS search_rank FROM recipes WHERE LOWER(title) LIKE CONCAT('%', LOWER(:title), '%') AND recipe_status_id = :statusId AND created_by = :createdById
            ) t
            GROUP BY id
            ORDER BY search_rank, title
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes
            WHERE LOWER(title) LIKE CONCAT('%', LOWER(:title), '%')
            AND recipe_status_id = :statusId
            AND created_by = :createdById
        """,
        nativeQuery = true
    )
    Page<Recipe> searchByTitleAndStatusIdAndCreatedById(@Param("title") String title, @Param("statusId") Long statusId, @Param("createdById") Long createdById, Pageable pageable);

    Page<Recipe> findByRecipeStatusIdAndRecipeCategoriesId(Long recipeStatusId, Long categoryId, Pageable pageable);

    Page<Recipe> findByRecipeStatusIdAndCreatedByIdAndRecipeCategoriesId(Long recipeStatusId, Long createdById, Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT r.*, 1 AS search_rank FROM recipes r
                    JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) = LOWER(:title) AND r.recipe_status_id = :statusId AND rrc.recipe_category_id = :categoryId
                UNION
                SELECT r.*, 2 AS search_rank FROM recipes r
                    JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) LIKE CONCAT(LOWER(:title), '%') AND r.recipe_status_id = :statusId AND rrc.recipe_category_id = :categoryId
                UNION
                SELECT r.*, 3 AS search_rank FROM recipes r
                    JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%') AND r.recipe_status_id = :statusId AND rrc.recipe_category_id = :categoryId
            ) t
            GROUP BY id
            ORDER BY search_rank, title
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes r
            JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
            WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%')
            AND r.recipe_status_id = :statusId
            AND rrc.recipe_category_id = :categoryId
        """,
        nativeQuery = true
    )
    Page<Recipe> searchByTitleAndStatusIdAndCategoryId(@Param("title") String title, @Param("statusId") Long statusId, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT r.*, 1 AS search_rank FROM recipes r
                    JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) = LOWER(:title) AND r.recipe_status_id = :statusId AND r.created_by = :createdById AND rrc.recipe_category_id = :categoryId
                UNION
                SELECT r.*, 2 AS search_rank FROM recipes r
                    JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) LIKE CONCAT(LOWER(:title), '%') AND r.recipe_status_id = :statusId AND r.created_by = :createdById AND rrc.recipe_category_id = :categoryId
                UNION
                SELECT r.*, 3 AS search_rank FROM recipes r
                    JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%') AND r.recipe_status_id = :statusId AND r.created_by = :createdById AND rrc.recipe_category_id = :categoryId
            ) t
            GROUP BY id
            ORDER BY search_rank, title
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes r
            JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
            WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%')
            AND r.recipe_status_id = :statusId
            AND r.created_by = :createdById
            AND rrc.recipe_category_id = :categoryId
        """,
        nativeQuery = true
    )
    Page<Recipe> searchByTitleAndStatusIdAndCreatedByIdAndCategoryId(@Param("title") String title, @Param("statusId") Long statusId, @Param("createdById") Long createdById, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT r.* FROM recipes r
            INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
            INNER JOIN (
                SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                FROM recipe_review_logs rrl2
                INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                GROUP BY rrl2.recipe_id
            ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
            WHERE r.recipe_status_id = :statusId
            AND rrl.reviewer_id = :reviewerId
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes r
            INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
            INNER JOIN (
                SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                FROM recipe_review_logs rrl2
                INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                GROUP BY rrl2.recipe_id
            ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
            WHERE r.recipe_status_id = :statusId
            AND rrl.reviewer_id = :reviewerId
        """,
        nativeQuery = true
    )
    Page<Recipe> findByRecipeStatusIdAndReviewerId(@Param("statusId") Long statusId, @Param("reviewerId") Long reviewerId, Pageable pageable);

    @Query(
        value = """
            SELECT r.* FROM recipes r
            INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
            INNER JOIN (
                SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                FROM recipe_review_logs rrl2
                INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                GROUP BY rrl2.recipe_id
            ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
            INNER JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
            WHERE r.recipe_status_id = :statusId
            AND rrl.reviewer_id = :reviewerId
            AND rrc.recipe_category_id = :categoryId
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes r
            INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
            INNER JOIN (
                SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                FROM recipe_review_logs rrl2
                INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                GROUP BY rrl2.recipe_id
            ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
            INNER JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
            WHERE r.recipe_status_id = :statusId
            AND rrl.reviewer_id = :reviewerId
            AND rrc.recipe_category_id = :categoryId
        """,
        nativeQuery = true
    )
    Page<Recipe> findByRecipeStatusIdAndReviewerIdAndRecipeCategoriesId(@Param("statusId") Long statusId, @Param("reviewerId") Long reviewerId, @Param("categoryId") Long categoryId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT r.*, 1 AS search_rank FROM recipes r
                    INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
                    INNER JOIN (
                        SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                        FROM recipe_review_logs rrl2
                        INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                        WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                        GROUP BY rrl2.recipe_id
                    ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
                    WHERE LOWER(r.title) = LOWER(:title) AND r.recipe_status_id = :statusId AND rrl.reviewer_id = :reviewerId
                UNION
                SELECT r.*, 2 AS search_rank FROM recipes r
                    INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
                    INNER JOIN (
                        SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                        FROM recipe_review_logs rrl2
                        INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                        WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                        GROUP BY rrl2.recipe_id
                    ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
                    WHERE LOWER(r.title) LIKE CONCAT(LOWER(:title), '%') AND r.recipe_status_id = :statusId AND rrl.reviewer_id = :reviewerId
                UNION
                SELECT r.*, 3 AS search_rank FROM recipes r
                    INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
                    INNER JOIN (
                        SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                        FROM recipe_review_logs rrl2
                        INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                        WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                        GROUP BY rrl2.recipe_id
                    ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
                    WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%') AND r.recipe_status_id = :statusId AND rrl.reviewer_id = :reviewerId
            ) t
            GROUP BY id
            ORDER BY search_rank, title
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes r
            INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
            INNER JOIN (
                SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                FROM recipe_review_logs rrl2
                INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                GROUP BY rrl2.recipe_id
            ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
            WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%')
            AND r.recipe_status_id = :statusId
            AND rrl.reviewer_id = :reviewerId
        """,
        nativeQuery = true
    )
    Page<Recipe> searchByTitleAndStatusIdAndReviewerId(@Param("title") String title, @Param("statusId") Long statusId, @Param("reviewerId") Long reviewerId, Pageable pageable);

    @Query(
        value = """
            SELECT * FROM (
                SELECT r.*, 1 AS search_rank FROM recipes r
                    INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
                    INNER JOIN (
                        SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                        FROM recipe_review_logs rrl2
                        INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                        WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                        GROUP BY rrl2.recipe_id
                    ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
                    INNER JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) = LOWER(:title) AND r.recipe_status_id = :statusId AND rrl.reviewer_id = :reviewerId AND rrc.recipe_category_id = :categoryId
                UNION
                SELECT r.*, 2 AS search_rank FROM recipes r
                    INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
                    INNER JOIN (
                        SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                        FROM recipe_review_logs rrl2
                        INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                        WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                        GROUP BY rrl2.recipe_id
                    ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
                    INNER JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) LIKE CONCAT(LOWER(:title), '%') AND r.recipe_status_id = :statusId AND rrl.reviewer_id = :reviewerId AND rrc.recipe_category_id = :categoryId
                UNION
                SELECT r.*, 3 AS search_rank FROM recipes r
                    INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
                    INNER JOIN (
                        SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                        FROM recipe_review_logs rrl2
                        INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                        WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                        GROUP BY rrl2.recipe_id
                    ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
                    INNER JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
                    WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%') AND r.recipe_status_id = :statusId AND rrl.reviewer_id = :reviewerId AND rrc.recipe_category_id = :categoryId
            ) t
            GROUP BY id
            ORDER BY search_rank, title
        """,
        countQuery = """
            SELECT COUNT(*) FROM recipes r
            INNER JOIN recipe_review_logs rrl ON rrl.recipe_id = r.id
            INNER JOIN (
                SELECT rrl2.recipe_id, MAX(rrl2.id) AS max_id
                FROM recipe_review_logs rrl2
                INNER JOIN recipe_review_events rre2 ON rrl2.recipe_review_event_id = rre2.id
                WHERE rre2.event = 'SUBMITTED_FOR_APPROVAL'
                GROUP BY rrl2.recipe_id
            ) latest ON latest.recipe_id = r.id AND latest.max_id = rrl.id
            INNER JOIN recipes_recipe_categories rrc ON rrc.recipe_id = r.id
            WHERE LOWER(r.title) LIKE CONCAT('%', LOWER(:title), '%')
            AND r.recipe_status_id = :statusId
            AND rrl.reviewer_id = :reviewerId
            AND rrc.recipe_category_id = :categoryId
        """,
        nativeQuery = true
    )
    Page<Recipe> searchByTitleAndStatusIdAndReviewerIdAndCategoryId(@Param("title") String title, @Param("statusId") Long statusId, @Param("reviewerId") Long reviewerId, @Param("categoryId") Long categoryId, Pageable pageable);

    boolean existsByTitleAndActiveTrue(String title);

    boolean existsByTitleAndActiveTrueAndIdNot(String title, Long id);
}
