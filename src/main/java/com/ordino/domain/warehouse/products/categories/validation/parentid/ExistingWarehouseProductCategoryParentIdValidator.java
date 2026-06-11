package com.ordino.domain.warehouse.products.categories.validation.parentid;

import com.ordino.domain.warehouse.products.categories.repository.WarehouseProductCategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingWarehouseProductCategoryParentIdValidator implements ConstraintValidator<ExistingWarehouseProductCategoryParentId, Long> {

    @Autowired
    private WarehouseProductCategoryRepository repository;

    @Override
    public boolean isValid(Long parentId, ConstraintValidatorContext context) {
        if (parentId == null || parentId <= 0) {
            return true;
        }
        return repository.existsById(parentId);
    }
}
