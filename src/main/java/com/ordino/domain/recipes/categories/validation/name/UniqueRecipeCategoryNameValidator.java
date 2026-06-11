package com.ordino.domain.recipes.categories.validation.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.recipes.categories.repository.RecipeCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueRecipeCategoryNameValidator extends UniquePropertyValidator<UniqueRecipeCategoryName> {

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return recipeCategoryRepository.existsByCategory(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return recipeCategoryRepository.existsByCategoryAndIdNot(value, excludeId);
    }
}
