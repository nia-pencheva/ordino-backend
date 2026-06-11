package com.ordino.domain.recipes.categories.model.dto;

import com.ordino.domain.recipes.categories.validation.name.ValidRecipeCategoryName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeCategoryRequestDTO {
    @ValidRecipeCategoryName
    private String category;
}
