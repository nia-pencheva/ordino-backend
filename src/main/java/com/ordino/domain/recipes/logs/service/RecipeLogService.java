package com.ordino.domain.recipes.logs.service;

import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.Tuple;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ordino.core.exception.ValidationException;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.recipes.logs.archive.model.entity.RecipeArchiveLog;
import com.ordino.domain.recipes.logs.archive.repository.RecipeArchiveLogRepository;
import com.ordino.domain.recipes.logs.edits.model.entity.RecipeEditLog;
import com.ordino.domain.recipes.logs.edits.repository.RecipeEditLogRepository;
import com.ordino.domain.recipes.logs.model.dto.RecipeLogPageEntryResponseDTO;
import com.ordino.domain.recipes.logs.model.dto.RecipeLogPageResponseDTO;
import com.ordino.domain.recipes.logs.repository.RecipeLogRepository;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewReturnedForRevisionLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewLog;
import com.ordino.domain.recipes.logs.review.repository.RecipeReviewLogRepository;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.units.model.entity.Unit;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeLogService {
    private final ObjectMapper objectMapper;
    private final RecipeLogRepository queryRepository;
    private final RecipeEditLogRepository editLogRepository;
    private final RecipeReviewLogRepository reviewLogRepository;
    private final RecipeArchiveLogRepository archiveLogRepository;

    @Transactional(readOnly = true)
    public RecipeLogPageResponseDTO getRecipeLogPage(Long id, Integer page, Integer pageSize, Instant from, Instant to) {
        if (from != null && to != null && !from.isBefore(to)) {
            throw new ValidationException("from", "'From' must be before 'To'");
        }

        int pageIndex = page != null ? page - 1 : 0;
        int offset = pageIndex * pageSize;

        List<Tuple> rows = queryRepository.findPagedLogEntries(id, pageSize, offset, from, to);

        List<Long> editIds = new ArrayList<>();
        List<Long> reviewIds = new ArrayList<>();
        List<Long> archiveIds = new ArrayList<>();

        for (Tuple row : rows) {
            Long entryId = ((Number) row.get("id")).longValue();
            switch (row.get("type", String.class)) {
                case "EDIT"    -> editIds.add(entryId);
                case "REVIEW"  -> reviewIds.add(entryId);
                case "ARCHIVE" -> archiveIds.add(entryId);
            }
        }

        Map<Long, RecipeEditLog> editMap = editLogRepository.findAllById(editIds).stream()
                .collect(Collectors.toMap(RecipeEditLog::getId, log -> log));
        Map<Long, RecipeReviewLog> reviewMap = reviewLogRepository.findAllById(reviewIds).stream()
                .collect(Collectors.toMap(RecipeReviewLog::getId, log -> log));
        Map<Long, RecipeArchiveLog> archiveMap = archiveLogRepository.findAllById(archiveIds).stream()
                .collect(Collectors.toMap(RecipeArchiveLog::getId, log -> log));

        List<RecipeLogPageEntryResponseDTO> entries = new ArrayList<>();
        for (Tuple row : rows) {
            Long entryId = ((Number) row.get("id")).longValue();
            RecipeLogPageEntryResponseDTO entry = new RecipeLogPageEntryResponseDTO();
            entry.setId(entryId);
            switch (row.get("type", String.class)) {
                case "EDIT" -> {
                    RecipeEditLog log = editMap.get(entryId);
                    entry.setEventType("EDITED");
                    entry.setUserFullName(log.getUser().getFullName());
                    entry.setCreatedAt(log.getCreatedAt());
                }
                case "REVIEW" -> {
                    RecipeReviewLog log = reviewMap.get(entryId);
                    entry.setEventType(log.getRecipeReviewEvent().getEvent());
                    entry.setUserFullName(("SUBMITTED_FOR_APPROVAL".equals(log.getRecipeReviewEvent().getEvent())) ? log.getRecipe().getCreatedBy().getFullName() : log.getReviewer().getFullName());
                    entry.setCreatedAt(log.getCreatedAt());
                }
                case "ARCHIVE" -> {
                    RecipeArchiveLog log = archiveMap.get(entryId);
                    entry.setEventType(log.getRecipeArchiveEvent().getEvent());
                    entry.setUserFullName(log.getUser().getFullName());
                    entry.setCreatedAt(log.getCreatedAt());
                }
            }
            entries.add(entry);
        }

        long totalElements = queryRepository.countLogEntries(id, from, to);

        RecipeLogPageResponseDTO dto = new RecipeLogPageResponseDTO();
        dto.setEntries(entries);
        dto.setTotalElements(totalElements);
        dto.setTotalPages((int) Math.ceil((double) totalElements / pageSize));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<RecipeReviewReturnedForRevisionLogEntryResponseDTO> getRevisionNotes(Long recipeId) {
        return reviewLogRepository
                .findByRecipeIdAndRecipeReviewEventEventOrderByCreatedAtDesc(recipeId, "RETURNED_FOR_REVISION")
                .stream()
                .map(this::mapReturnedLog)
                .collect(Collectors.toList());
    }

    private RecipeReviewReturnedForRevisionLogEntryResponseDTO mapReturnedLog(RecipeReviewLog log) {
        RecipeReviewReturnedForRevisionLogEntryResponseDTO dto = new RecipeReviewReturnedForRevisionLogEntryResponseDTO();
        fillReviewDto(dto, log);
        dto.setNotes(log.getReturnNotes());
        return dto;
    }

    private void fillReviewDto(RecipeReviewLogEntryResponseDTO dto, RecipeReviewLog log) {
        dto.setId(log.getId());
        dto.setCreatedAt(log.getCreatedAt());
        dto.setUserId(log.getReviewer().getId());
        dto.setUserFullName(log.getReviewer().getFullName());
    }

    public String createRecipeSnapshot(Recipe recipe) throws JsonProcessingException {
        ObjectNode recipeNode = objectMapper.createObjectNode();
        recipeNode.put("title", recipe.getTitle());
        recipeNode.put("preparationTime", recipe.getPreparationTime());
        recipeNode.put("servings", recipe.getServings());
        recipeNode.put("instructions", recipe.getInstructions());
        recipeNode.put("notes", recipe.getNotes());
        recipeNode.put("description", recipe.getDescription());

        ArrayNode productsNode = objectMapper.createArrayNode();
        recipe.getRecipeProducts()
                .stream()
                .sorted(Comparator.comparingInt(join -> join.getPosition()))
                .forEach(join -> {
                    Product product = join.getProduct();
                    Unit unit = join.getUnit();

                    ObjectNode productNode = objectMapper.createObjectNode();
                    productNode.put("productName", product.getName());
                    productNode.put("position", join.getPosition());
                    productNode.put("quantity", join.getQuantity().setScale(3, RoundingMode.UNNECESSARY));
                    productNode.put("unitAbbreviation", unit.getAbbreviation());

                    productsNode.add(productNode);
                });

        ArrayNode categories = objectMapper.createArrayNode();
        recipe.getRecipeCategories()
                .stream()
                .sorted(Comparator.comparing(category -> category.getCategory()))
                .forEach(category -> {
                    ObjectNode categoryNode = objectMapper.createObjectNode();
                    categoryNode.put("category", category.getCategory());
                    categories.add(categoryNode);
                });

        recipeNode.set("products", productsNode);
        recipeNode.set("categories", categories);

        return objectMapper.writeValueAsString(recipeNode);
    }
}
