package com.ordino.domain.recipes.products.categories.validation.id;

import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeafRecipeIngredientCategoryValidator implements ConstraintValidator<LeafRecipeIngredientCategory, Long> {

    @Autowired
    private RecipeIngredientCategoryRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null || id <= 0) {
            return true;
        }
        return !repository.existsByParentCategoryId(id);
    }
}
