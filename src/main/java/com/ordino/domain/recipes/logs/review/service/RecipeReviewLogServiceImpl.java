package com.ordino.domain.recipes.logs.review.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewApprovedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewDiscardedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewReturnedForRevisionLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewSubmittedForApprovalLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewEvent;
import com.ordino.domain.recipes.logs.review.model.entity.RecipeReviewLog;
import com.ordino.domain.recipes.logs.review.repository.RecipeReviewEventRepository;
import com.ordino.domain.recipes.logs.review.repository.RecipeReviewLogRepository;
import com.ordino.domain.recipes.logs.service.RecipeLogService;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.users.model.entity.User;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeReviewLogServiceImpl implements RecipeReviewLogService {
    private final RecipeReviewLogRepository recipeReviewLogRepository;
    private final RecipeReviewEventRepository recipeReviewEventRepository;
    private final RecipeLogService recipeLogService;

    public RecipeReviewSubmittedForApprovalLogEntryResponseDTO getSubmittedForApprovalLogEntry(Long id) {
        RecipeReviewLog logEntry = recipeReviewLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe review log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User creator = logEntry.getRecipe().getCreatedBy();

        RecipeReviewSubmittedForApprovalLogEntryResponseDTO responseDTO = new RecipeReviewSubmittedForApprovalLogEntryResponseDTO();
        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(creator.getId());
        responseDTO.setUserFullName(creator.getFullName());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());
        responseDTO.setSnapshot(logEntry.getSnapshot());

        return responseDTO;
    }

    public RecipeReviewReturnedForRevisionLogEntryResponseDTO getReturnedForRevisionLogEntry(Long id) {
        RecipeReviewLog logEntry = recipeReviewLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe review log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User reviewer = logEntry.getReviewer();

        RecipeReviewReturnedForRevisionLogEntryResponseDTO responseDTO = new RecipeReviewReturnedForRevisionLogEntryResponseDTO();
        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(reviewer.getId());
        responseDTO.setUserFullName(reviewer.getFullName());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());
        responseDTO.setNotes(logEntry.getReturnNotes());

        return responseDTO;
    }

    public RecipeReviewDiscardedLogEntryResponseDTO getDiscardedLogEntry(Long id) {
        RecipeReviewLog logEntry = recipeReviewLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe review log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User reviewer = logEntry.getReviewer();

        RecipeReviewDiscardedLogEntryResponseDTO responseDTO = new RecipeReviewDiscardedLogEntryResponseDTO();
        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(reviewer.getId());
        responseDTO.setUserFullName(reviewer.getFullName());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());

        return responseDTO;
    }

    public RecipeReviewApprovedLogEntryResponseDTO getApprovedLogEntry(Long id) {
        RecipeReviewLog logEntry = recipeReviewLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Recipe review log entry not found"));

        Recipe recipe = logEntry.getRecipe();
        User reviewer = logEntry.getReviewer();

        RecipeReviewApprovedLogEntryResponseDTO responseDTO = new RecipeReviewApprovedLogEntryResponseDTO();
        responseDTO.setRecipeId(recipe.getId());
        responseDTO.setRecipeTitle(recipe.getTitle());
        responseDTO.setUserId(reviewer.getId());
        responseDTO.setUserFullName(reviewer.getFullName());
        responseDTO.setCreatedAt(logEntry.getCreatedAt());
        responseDTO.setSnapshot(logEntry.getSnapshot());

        return responseDTO;
    }

    public void createLog(Recipe recipe, User reviewer, String eventName) throws JsonProcessingException {
        createLog(recipe, reviewer, eventName, null);
    }

    public void createLog(Recipe recipe, User reviewer, String eventName, String returnNotes) throws JsonProcessingException {
        RecipeReviewEvent event = recipeReviewEventRepository.findByEvent(eventName)
            .orElseThrow(() -> new EntityNotFoundException("Review event not found: " + eventName));

        RecipeReviewLog log = new RecipeReviewLog();
        log.setRecipe(recipe);
        log.setReviewer(reviewer);
        log.setSnapshot(recipeLogService.createRecipeSnapshot(recipe));
        log.setRecipeReviewEvent(event);
        log.setReturnNotes(returnNotes);

        recipeReviewLogRepository.save(log);
    }
}
