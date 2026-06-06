package com.ordino.domain.products.validation.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.products.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueProductNameValidator extends UniquePropertyValidator<UniqueProductName> {

    @Autowired
    private ProductRepository productRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return productRepository.existsByName(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return productRepository.existsByNameAndIdNot(value, excludeId);
    }
}
