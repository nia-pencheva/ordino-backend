package com.ordino.domain.recipes.products.categories.model.dto.move;

import com.ordino.domain.recipes.products.categories.validation.parentid.ValidRecipeIngredientCategoryParentId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveRecipeIngredientCategoryRequestDTO {
    @ValidRecipeIngredientCategoryParentId
    private Long parentId;
}
