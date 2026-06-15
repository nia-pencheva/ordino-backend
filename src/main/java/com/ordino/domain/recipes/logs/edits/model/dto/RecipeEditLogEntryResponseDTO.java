package com.ordino.domain.recipes.logs.edits.model.dto;

import com.ordino.domain.recipes.logs.model.dto.RecipeLogEntryResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeEditLogEntryResponseDTO extends RecipeLogEntryResponseDTO {
    private String oldData;
    private String newData;
}
