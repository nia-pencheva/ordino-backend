package com.ordino.domain.units.validation.category.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.units.repository.UnitCategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUnitCategoryNameValidator extends UniquePropertyValidator<UniqueUnitCategoryName> {

    @Autowired
    private UnitCategoryRepository unitCategoryRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return unitCategoryRepository.existsByCategory(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return unitCategoryRepository.existsByCategoryAndIdNot(value, excludeId);
    }
}
