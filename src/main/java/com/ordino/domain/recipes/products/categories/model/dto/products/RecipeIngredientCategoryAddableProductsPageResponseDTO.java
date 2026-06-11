package com.ordino.domain.recipes.products.categories.model.dto.products;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientCategoryAddableProductsPageResponseDTO {
    private List<RecipeIngredientCategoryAddableProductResponseDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
