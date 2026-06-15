package com.ordino.domain.recipes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.ordino.domain.recipes.categories.validation.id.ExistingRecipeCategoryId;
import com.ordino.domain.recipes.model.dto.edit.RecipeEditDataResponseDTO;
import com.ordino.domain.recipes.model.dto.RecipeResponseDTO;
import com.ordino.domain.recipes.model.dto.RecipesPageResponseDTO;
import com.ordino.domain.recipes.model.dto.draft.SaveDraftRequestDTO;
import com.ordino.domain.recipes.model.dto.edit.RecipeForEditingResponseDTO;
import com.ordino.domain.recipes.model.dto.review.ReturnRecipeForRevisionRequestDTO;
import com.ordino.domain.recipes.model.dto.review.SubmitRecipeRequestDTO;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestDTO;
import com.ordino.domain.recipes.service.RecipeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/recipes")
@AllArgsConstructor
@Validated
public class RecipesController {
    private RecipeService recipeService;

    @GetMapping()
    public ResponseEntity<RecipesPageResponseDTO> getRecipes(
        @RequestParam(required = false) String searchTitle,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam @Positive Integer pageSize,
        @RequestParam @NotBlank String recipeStatus,
        @RequestParam(required = false) @Positive @ExistingRecipeCategoryId Long recipeCategoryId
    ) {
        return ResponseEntity.ok().body(recipeService.getRecipes(searchTitle, page, pageSize, recipeStatus, recipeCategoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> getRecipe(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(recipeService.getRecipe(id));
    }

    /* @GetMapping("/{id}/history")
    public ResponseEntity<List<RecipeHistoryEntryResponseDTO>> getRecipeHistory(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(recipeService.getRecipeHistory(id));
    } */

    @PostMapping("/draft")
    public ResponseEntity<Long> createDraft(@Valid @RequestBody SaveDraftRequestDTO dto) {
        return ResponseEntity.ok().body(recipeService.createDraft(dto));
    }

    @GetMapping("/edit-data")
    public ResponseEntity<RecipeEditDataResponseDTO> getEditData() {
        return ResponseEntity.ok().body(recipeService.getEditData());
    }

    @GetMapping("/{id}/edit")
    public ResponseEntity<RecipeForEditingResponseDTO> getRecipeForEditing(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(recipeService.getRecipeForEditing(id));
    }

    @PostMapping("/draft/{id}")
    public ResponseEntity<Void> saveDraft(@PathVariable @Positive Long id, @Valid @RequestBody SaveDraftRequestDTO dto) {
        recipeService.saveDraft(id, dto);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/draft/{id}")
    public ResponseEntity<Void> deleteDraft(@PathVariable @Positive Long id) {
        recipeService.deleteDraft(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<Void> submitForApproval(@PathVariable @Positive Long id, @Valid @RequestBody SubmitRecipeRequestDTO dto) throws JsonProcessingException {
        recipeService.submitForApproval(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/review")
    public ResponseEntity<RecipeResponseDTO> getRecipeForReview(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(recipeService.getRecipeForReview(id));
    }

    @PostMapping("/{id}/return-for-revision")
    public ResponseEntity<Void> returnRecipeForRevision(@PathVariable @Positive Long id, @Valid @RequestBody ReturnRecipeForRevisionRequestDTO dto) throws JsonProcessingException {
        recipeService.returnRecipeForRevision(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/discard")
    public ResponseEntity<Void> discardRecipe(@PathVariable @Positive Long id) throws JsonProcessingException {
        recipeService.discardRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/self-approve")
    public ResponseEntity<Void> selfApproveRecipe(@PathVariable @Positive Long id) throws JsonProcessingException {
        recipeService.selfApproveRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveRecipe(@PathVariable @Positive Long id) throws JsonProcessingException {
        recipeService.approveRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveRecipe(@PathVariable @Positive Long id, @Valid @RequestBody SaveRecipeRequestDTO dto) {
        recipeService.saveRecipe(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<Void> archiveRecipe(@PathVariable @Positive Long id) {
        recipeService.archiveRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchiveRecipe(@PathVariable @Positive Long id) {
        recipeService.unarchiveRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
