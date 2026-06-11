package com.ordino.domain.recipes.products.categories.validation.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueRecipeIngredientCategoryNameValidator extends UniquePropertyValidator<UniqueRecipeIngredientCategoryName> {

    @Autowired
    private RecipeIngredientCategoryRepository repository;

    @Override
    protected boolean existsByProperty(String value) {
        return repository.existsByCategory(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return repository.existsByCategoryAndIdNot(value, excludeId);
    }
}
