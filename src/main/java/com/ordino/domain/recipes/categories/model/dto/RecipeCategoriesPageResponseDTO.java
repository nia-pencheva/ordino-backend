package com.ordino.domain.recipes.categories.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeCategoriesPageResponseDTO {
    private List<RecipeCategoryResponseDTO> recipeCategories;
    private Long totalElements;
    private Integer totalPages;
}
