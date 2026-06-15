package com.ordino.domain.recipes.model.dto.draft;

import java.util.List;

import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestProductDTO;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestCategoryDTO;
import com.ordino.domain.recipes.validation.ValidTitle;
import com.ordino.domain.recipes.validation.categories.no_duplicate_ids.NoDuplicateCategoryIds;
import com.ordino.domain.recipes.validation.draft.ValidDraftPreparationTime;
import com.ordino.domain.recipes.validation.draft.ValidDraftServings;
import com.ordino.domain.recipes.validation.products.no_duplicate_ids.NoDuplicateProductIds;
import com.ordino.domain.recipes.validation.products.no_duplicate_positions.NoDuplicateProductPositions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveDraftRequestDTO {
    @ValidTitle
    private String title;

    @ValidDraftPreparationTime
    private Integer preparationTime;

    @ValidDraftServings
    private Integer servings;

    private String instructions;

    private String notes;

    private String description;

    @Valid
    @NoDuplicateProductPositions
    @NoDuplicateProductIds
    private List<SaveRecipeRequestProductDTO> recipeProducts;

    @Valid
    @NoDuplicateCategoryIds
    private List<SaveRecipeRequestCategoryDTO> recipeCategories;
}
