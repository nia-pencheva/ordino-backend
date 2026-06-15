package com.ordino.domain.recipes.model.dto.edit;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeForEditingResponseUnitCategoriesForSelectDTO {
    private Long id;
    private String category;
    private List<RecipeForEditingResponseUnitForSelectDTO> units;
}
