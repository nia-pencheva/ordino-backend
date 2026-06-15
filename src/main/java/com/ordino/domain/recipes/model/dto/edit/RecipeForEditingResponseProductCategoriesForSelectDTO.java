package com.ordino.domain.recipes.model.dto.edit;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeForEditingResponseProductCategoriesForSelectDTO {
    private Long id;
    private String category;
    private List<RecipeForEditingResponseProductCategoriesForSelectDTO> subCategories;
    private List<RecipeForEditingResponseProductCategoriesForSelectProductDTO> products;
}
