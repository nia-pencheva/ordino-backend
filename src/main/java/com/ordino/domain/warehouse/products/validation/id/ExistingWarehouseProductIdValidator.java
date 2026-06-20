package com.ordino.domain.warehouse.products.validation.id;

import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingWarehouseProductIdValidator implements ConstraintValidator<ExistingWarehouseProductId, Long> {

    @Autowired
    private WarehouseProductRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return repository.existsById(id);
    }
}
