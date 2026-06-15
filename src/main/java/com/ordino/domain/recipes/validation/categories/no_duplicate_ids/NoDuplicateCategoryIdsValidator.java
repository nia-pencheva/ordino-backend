package com.ordino.domain.recipes.validation.categories.no_duplicate_ids;

import java.util.List;
import java.util.Objects;

import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestCategoryDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoDuplicateCategoryIdsValidator
        implements ConstraintValidator<NoDuplicateCategoryIds, List<SaveRecipeRequestCategoryDTO>> {

    @Override
    public boolean isValid(List<SaveRecipeRequestCategoryDTO> categories, ConstraintValidatorContext context) {
        if (categories == null || categories.isEmpty()) {
            return true;
        }
        long nonNullCount = categories.stream()
                .map(SaveRecipeRequestCategoryDTO::getRecipeCategoryId)
                .filter(Objects::nonNull)
                .count();
        long distinctCount = categories.stream()
                .map(SaveRecipeRequestCategoryDTO::getRecipeCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return distinctCount == nonNullCount;
    }
}
