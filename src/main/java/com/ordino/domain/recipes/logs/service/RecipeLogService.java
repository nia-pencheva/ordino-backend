package com.ordino.domain.recipes.logs.service;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.domain.recipes.logs.model.dto.RecipeLogPageResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewReturnedForRevisionLogEntryResponseDTO;
import com.ordino.domain.recipes.model.entity.Recipe;

public interface RecipeLogService {
    RecipeLogPageResponseDTO getRecipeLogPage(Long id, Integer page, Integer pageSize, Instant from, Instant to);

    List<RecipeReviewReturnedForRevisionLogEntryResponseDTO> getRevisionNotes(Long recipeId);

    String createRecipeSnapshot(Recipe recipe) throws JsonProcessingException;
}
