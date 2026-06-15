package com.ordino.domain.recipes.logs.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeLogPageResponseDTO {
    private List<RecipeLogPageEntryResponseDTO> entries;
    private Long totalElements;
    private Integer totalPages;
}
