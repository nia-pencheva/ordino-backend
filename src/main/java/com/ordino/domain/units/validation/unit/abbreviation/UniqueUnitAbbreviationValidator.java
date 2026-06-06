package com.ordino.domain.units.validation.unit.abbreviation;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.units.repository.UnitRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUnitAbbreviationValidator extends UniquePropertyValidator<UniqueUnitAbbreviation> {

    @Autowired
    private UnitRepository unitRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return unitRepository.existsByAbbreviation(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return unitRepository.existsByAbbreviationAndIdNot(value, excludeId);
    }
}
