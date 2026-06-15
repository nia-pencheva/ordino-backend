package com.ordino.domain.recipes.logs.model.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeLogEntryResponseDTO {
    private Long id;
    private Long recipeId;
    private String recipeTitle;
    private Long userId;
    private String userFullName;
    private Instant createdAt;
}
