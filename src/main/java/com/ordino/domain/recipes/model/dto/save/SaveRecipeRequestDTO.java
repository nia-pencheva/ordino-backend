package com.ordino.domain.recipes.model.dto.save;

import java.util.List;

import com.ordino.core.validation.NullOrNotBlank.NullOrNotBlank;
import com.ordino.domain.recipes.validation.ValidInstructions;
import com.ordino.domain.recipes.validation.ValidPreparationTime;
import com.ordino.domain.recipes.validation.ValidServings;
import com.ordino.domain.recipes.validation.ValidTitle;
import com.ordino.domain.recipes.validation.title.UniqueRecipeTitle;
import com.ordino.domain.recipes.validation.categories.no_duplicate_ids.NoDuplicateCategoryIds;
import com.ordino.domain.recipes.validation.products.no_duplicate_ids.NoDuplicateProductIds;
import com.ordino.domain.recipes.validation.products.no_duplicate_positions.NoDuplicateProductPositions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRecipeRequestDTO {
    @ValidTitle
    @UniqueRecipeTitle
    private String title;

    @ValidPreparationTime
    private Integer preparationTime;

    @ValidServings
    private Integer servings;

    @NotNull(message = "At least one step is required")
    @ValidInstructions
    private String instructions;

    @NullOrNotBlank(message = "Instructions must not be blank")
    private String notes;

    @NotNull(message = "Descripton are required")
    @NotBlank(message = "Descripton must not be blank")
    private String description;

    @Valid
    @NotEmpty(message = "At least one ingredient is required")
    @NoDuplicateProductPositions
    @NoDuplicateProductIds
    private List<SaveRecipeRequestProductDTO> recipeProducts;

    @Valid
    @NotEmpty(message = "At least one category is required")
    @NoDuplicateCategoryIds
    private List<SaveRecipeRequestCategoryDTO> recipeCategories;
}
