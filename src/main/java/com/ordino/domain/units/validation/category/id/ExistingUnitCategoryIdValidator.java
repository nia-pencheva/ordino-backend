package com.ordino.domain.units.validation.category.id;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordino.domain.units.repository.UnitCategoryRepository;

@Component
public class ExistingUnitCategoryIdValidator implements ConstraintValidator<ExistingUnitCategoryId, Long> {

    @Autowired
    private UnitCategoryRepository unitCategoryRepository;

    @Override
    public boolean isValid(Long categoryId, ConstraintValidatorContext context) {
        if (categoryId == null || categoryId <= 0) {
            return true;
        }
        return unitCategoryRepository.existsById(categoryId);
    }
}
