package com.ordino.domain.recipes.products.categories.model.dto.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientCategoryAddableProductResponseDTO {
    private Long id;
    private String name;
    private Boolean active; 
}
