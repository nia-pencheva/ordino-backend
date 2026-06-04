package com.ordino.domain.units.validation.unit;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.units.repository.UnitRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUnitNameValidator implements ConstraintValidator<UniqueUnitName, String> {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String unit, ConstraintValidatorContext context) {
        if (unit == null || unit.isBlank()) {
            return true;
        }
        Long excludeId = PathVariablesUtil.extractPathId(request);
        if (excludeId != null) {
            return !unitRepository.existsByUnitAndIdNot(unit, excludeId);
        }
        return !unitRepository.existsByUnit(unit);
    }
}
