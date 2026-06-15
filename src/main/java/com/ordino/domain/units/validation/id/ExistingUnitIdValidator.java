package com.ordino.domain.units.validation.id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ordino.domain.units.repository.UnitRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ExistingUnitIdValidator implements ConstraintValidator<ExistingUnitId, Long> {

    @Autowired
    private UnitRepository unitRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return unitRepository.existsById(id);
    }
}
