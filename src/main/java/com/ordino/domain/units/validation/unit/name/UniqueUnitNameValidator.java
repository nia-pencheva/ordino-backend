package com.ordino.domain.units.validation.unit.name;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.units.repository.UnitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUnitNameValidator extends UniquePropertyValidator<UniqueUnitName> {

    @Autowired
    private UnitRepository unitRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return unitRepository.existsByUnit(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return unitRepository.existsByUnitAndIdNot(value, excludeId);
    }
}
