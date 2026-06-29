package com.ordino.domain.warehouse.products.validation.id;

import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExistingActiveWarehouseProductIdValidator implements ConstraintValidator<ExistingActiveWarehouseProductId, Long> {

    private final WarehouseProductRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return repository.existsByIdAndActiveTrue(id);
    }
}
