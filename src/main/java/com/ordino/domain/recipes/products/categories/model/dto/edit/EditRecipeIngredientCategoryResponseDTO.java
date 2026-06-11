package com.ordino.domain.recipes.products.categories.model.dto.edit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditRecipeIngredientCategoryResponseDTO {
    private Long id;
    private String category;
    private Long parentCategoryId;
}
