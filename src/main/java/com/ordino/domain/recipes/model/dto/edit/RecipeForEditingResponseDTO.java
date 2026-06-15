package com.ordino.domain.recipes.model.dto.edit;

import java.util.List;

import com.ordino.domain.recipes.model.dto.RecipeResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeForEditingResponseDTO {
    private RecipeResponseDTO recipe;
    private List<RecipeForEditingResponseProductCategoriesForSelectDTO> productCategories;
    private List<RecipeForEditingResponseUnitCategoriesForSelectDTO> unitCategories;
}
