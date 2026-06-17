package com.ordino.domain.warehouse.products.validation.product_id;

import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueWarehouseProductProductIdValidator implements ConstraintValidator<UniqueWarehouseProductProductId, Long> {

    @Autowired
    private WarehouseProductRepository repository;

    @Override
    public boolean isValid(Long productId, ConstraintValidatorContext context) {
        if (productId == null) {
            return true;
        }
        return !repository.existsByProductId(productId);
    }
}
