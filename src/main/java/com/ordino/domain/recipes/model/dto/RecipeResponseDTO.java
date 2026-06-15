package com.ordino.domain.recipes.model.dto;

import java.util.List;

import com.ordino.domain.recipes.logs.review.model.dto.RecipeReviewReturnedForRevisionLogEntryResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeResponseDTO {
    private Long id;
    private String title;
    private Integer preparationTime;
    private Integer servings;
    private String instructions;
    private String notes;
    private String description;
    private String status;
    private String createdByFullName;
    private boolean createdByCurrentUser;
    private String approvedByFullName;
    private List<RecipeResponseProductDTO> products;
    private List<RecipeResponseRecipeCategoryDTO> recipeCategories;
    private List<RecipeReviewReturnedForRevisionLogEntryResponseDTO> revisionNotes;
}
