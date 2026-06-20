package com.ordino.domain.suppliers.validation.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.suppliers.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueSupplierNameValidator extends UniquePropertyValidator<UniqueSupplierName> {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return supplierRepository.existsByName(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return supplierRepository.existsByNameAndIdNot(value, excludeId);
    }
}
