package com.ordino.domain.recipes.categories.validation.id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordino.domain.recipes.categories.repository.RecipeCategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistingRecipeCategoryIdValidator implements ConstraintValidator<ExistingRecipeCategoryId, Long> {

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return recipeCategoryRepository.existsById(id);
    }
}
