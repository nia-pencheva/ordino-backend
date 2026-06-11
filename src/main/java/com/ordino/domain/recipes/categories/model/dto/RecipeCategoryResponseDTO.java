package com.ordino.domain.recipes.categories.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeCategoryResponseDTO {
    private Long id;
    private String category;
    private Integer recipesCount;
}
