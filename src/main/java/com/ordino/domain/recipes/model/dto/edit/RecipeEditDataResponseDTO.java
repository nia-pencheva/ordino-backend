package com.ordino.domain.recipes.model.dto.edit;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeEditDataResponseDTO {
    private List<RecipeForEditingResponseProductCategoriesForSelectDTO> productCategories;
    private List<RecipeForEditingResponseUnitCategoriesForSelectDTO> unitCategories;
}
