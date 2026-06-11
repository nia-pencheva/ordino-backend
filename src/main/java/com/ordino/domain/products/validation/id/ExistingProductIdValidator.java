package com.ordino.domain.products.validation.id;

import com.ordino.domain.products.repository.ProductRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExistingProductIdValidator implements ConstraintValidator<ExistingProductId, Long> {

    @Autowired
    private ProductRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return repository.existsById(id);
    }
}
