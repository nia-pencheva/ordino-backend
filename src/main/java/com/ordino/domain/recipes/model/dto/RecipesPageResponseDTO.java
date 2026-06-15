package com.ordino.domain.recipes.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipesPageResponseDTO {
    private List<RecipeResponseDTO> recipes;
    private Long totalElements;
    private Integer totalPages;
}
