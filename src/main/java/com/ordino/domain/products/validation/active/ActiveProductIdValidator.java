package com.ordino.domain.products.validation.active;

import com.ordino.domain.products.repository.ProductRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ActiveProductIdValidator implements ConstraintValidator<ActiveProductId, Long> {

    private final ProductRepository repository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return repository.existsByIdAndActiveTrue(id);
    }
}
