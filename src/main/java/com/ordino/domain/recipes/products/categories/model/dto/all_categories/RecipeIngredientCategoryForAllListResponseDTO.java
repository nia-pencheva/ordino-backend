package com.ordino.domain.recipes.products.categories.model.dto.all_categories;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientCategoryForAllListResponseDTO {
    private Long id;
    private String category;
    private List<RecipeIngredientCategoryForAllListResponseDTO> subCategories;
    private List<RecipeIngredientCategoryProductForAllListResponseDTO> products;
}
