package com.ordino.domain.recipes.products.categories.model.dto.save;

import com.ordino.domain.recipes.products.categories.validation.name.ValidRecipeIngredientCategoryName;
import com.ordino.domain.recipes.products.categories.validation.parentid.ValidRecipeIngredientCategoryParentId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRecipeIngredientCategoryRequestDTO {
    @ValidRecipeIngredientCategoryName
    private String category;

    @ValidRecipeIngredientCategoryParentId
    private Long parentId;
}
