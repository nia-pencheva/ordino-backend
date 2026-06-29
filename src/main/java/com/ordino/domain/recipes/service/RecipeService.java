package com.ordino.domain.recipes.service;

import com.ordino.domain.recipes.model.dto.RecipeResponseDTO;
import com.ordino.domain.recipes.model.dto.RecipesPageResponseDTO;
import com.ordino.domain.recipes.model.dto.draft.SaveDraftRequestDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeEditDataResponseDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseDTO;
import com.ordino.domain.recipes.model.dto.review.ReturnRecipeForRevisionRequestDTO;
import com.ordino.domain.recipes.model.dto.review.SubmitRecipeRequestDTO;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestDTO;

import jakarta.persistence.EntityNotFoundException;

public interface RecipeService {
    RecipesPageResponseDTO getRecipes(String searchTitle, Integer page, Integer pageSize, String recipeStatus, Long recipeCategoryId) throws EntityNotFoundException;

    Long createDraft(SaveDraftRequestDTO dto) throws EntityNotFoundException;

    RecipeResponseDTO getRecipe(Long id);

    RecipeEditDataResponseDTO getEditData();

    RecipeForEditingResponseDTO getRecipeForEditing(Long id);

    void saveDraft(Long id, SaveDraftRequestDTO dto) throws EntityNotFoundException;

    void deleteDraft(Long id) throws EntityNotFoundException;

    void submitForApproval(Long id, SubmitRecipeRequestDTO dto) throws EntityNotFoundException;

    RecipeResponseDTO getRecipeForReview(Long id) throws EntityNotFoundException;

    void returnRecipeForRevision(Long id, ReturnRecipeForRevisionRequestDTO dto) throws EntityNotFoundException;

    void discardRecipe(Long id) throws EntityNotFoundException;

    void selfApproveRecipe(Long id) throws EntityNotFoundException;

    void approveRecipe(Long id) throws EntityNotFoundException;

    void saveRecipe(Long id, SaveRecipeRequestDTO dto) throws EntityNotFoundException;

    void archiveRecipe(Long id) throws EntityNotFoundException;

    void unarchiveRecipe(Long id) throws EntityNotFoundException;
}
