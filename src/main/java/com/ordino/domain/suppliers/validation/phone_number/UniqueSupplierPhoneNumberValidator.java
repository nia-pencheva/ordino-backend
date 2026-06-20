package com.ordino.domain.suppliers.validation.phone_number;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.suppliers.repository.SupplierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueSupplierPhoneNumberValidator extends UniquePropertyValidator<UniqueSupplierPhoneNumber> {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return supplierRepository.existsByPhoneNumber(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return supplierRepository.existsByPhoneNumberAndIdNot(value, excludeId);
    }
}
