package com.ordino.domain.recipes.products.categories.validation.parentid;

import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingRecipeIngredientCategoryParentIdValidator implements ConstraintValidator<ExistingRecipeIngredientCategoryParentId, Long> {

    @Autowired
    private RecipeIngredientCategoryRepository repository;

    @Override
    public boolean isValid(Long parentId, ConstraintValidatorContext context) {
        if (parentId == null || parentId <= 0) {
            return true;
        }
        return repository.existsById(parentId);
    }
}
