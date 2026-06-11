package com.ordino.domain.warehouse.products.categories.validation.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.warehouse.products.categories.repository.WarehouseProductCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueWarehouseProductCategoryNameValidator extends UniquePropertyValidator<UniqueWarehouseProductCategoryName> {

    @Autowired
    private WarehouseProductCategoryRepository repository;

    @Override
    protected boolean existsByProperty(String value) {
        return repository.existsByCategory(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return repository.existsByCategoryAndIdNot(value, excludeId);
    }
}
