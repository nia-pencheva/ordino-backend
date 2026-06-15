package com.ordino.domain.recipes.model.dto.edit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeForEditingResponseUnitForSelectDTO {
    private Long id;
    private String unit;
    private String abbreviation;
}
