package com.ordino.domain.recipes.validation.products.no_duplicate_positions;

import java.util.List;
import java.util.Objects;

import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestProductDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoDuplicateProductPositionsValidator
        implements ConstraintValidator<NoDuplicateProductPositions, List<SaveRecipeRequestProductDTO>> {

    @Override
    public boolean isValid(List<SaveRecipeRequestProductDTO> products, ConstraintValidatorContext context) {
        if (products == null || products.isEmpty()) {
            return true;
        }
        long nonNullCount = products.stream()
                .map(SaveRecipeRequestProductDTO::getPosition)
                .filter(Objects::nonNull)
                .count();
        long distinctCount = products.stream()
                .map(SaveRecipeRequestProductDTO::getPosition)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return distinctCount == nonNullCount;
    }
}
