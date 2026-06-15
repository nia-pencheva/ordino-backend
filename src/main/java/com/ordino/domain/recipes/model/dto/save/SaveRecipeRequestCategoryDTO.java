package com.ordino.domain.recipes.model.dto.save;

import com.ordino.domain.recipes.categories.validation.id.ExistingRecipeCategoryId;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRecipeRequestCategoryDTO {
    @NotNull(message = "Category ID is required")
    @ExistingRecipeCategoryId
    private Long recipeCategoryId;
}
