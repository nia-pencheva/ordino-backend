package com.ordino.domain.units.validation.abbreviation;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.units.repository.UnitRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUnitAbbreviationValidator implements ConstraintValidator<UniqueUnitAbbreviation, String> {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String abbreviation, ConstraintValidatorContext context) {
        if (abbreviation == null || abbreviation.isBlank()) {
            return true;
        }
        Long excludeId = PathVariablesUtil.extractPathId(request);
        if (excludeId != null) {
            return !unitRepository.existsByAbbreviationAndIdNot(abbreviation, excludeId);
        }
        return !unitRepository.existsByAbbreviation(abbreviation);
    }
}
