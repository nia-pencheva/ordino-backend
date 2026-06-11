package com.ordino.domain.warehouse.products.categories.validation.id;

import com.ordino.domain.warehouse.products.categories.repository.WarehouseProductCategoryRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LeafWarehouseProductCategoryValidator implements ConstraintValidator<LeafWarehouseProductCategory, Long> {

    @Autowired
    private WarehouseProductCategoryRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null || id <= 0) {
            return true;
        }
        return !repository.existsByParentCategoryId(id);
    }
}
