package com.ordino.domain.recipes.validation.title;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.recipes.repository.RecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueRecipeTitleValidator extends UniquePropertyValidator<UniqueRecipeTitle> {

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return recipeRepository.existsByTitleAndActiveTrue(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return recipeRepository.existsByTitleAndActiveTrueAndIdNot(value, excludeId);
    }
}
