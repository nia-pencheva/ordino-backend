package com.ordino.domain.suppliers.validation.id;

import org.springframework.stereotype.Component;

import com.ordino.domain.suppliers.repository.SupplierRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ExistingActiveSupplierIdValidator implements ConstraintValidator<ExistingActiveSupplierId, Long> {

    private final SupplierRepository supplierRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return supplierRepository.existsByIdAndActive(id, true);
    }
}
