package com.ordino.domain.recipes.logs.model.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeLogPageEntryResponseDTO {
    private Long id;
    private String userFullName;
    private String eventType;
    private Instant createdAt;
}
