package com.ordino.domain.warehouse.loss_reasons.validation.id;

import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingLossReasonIdValidator implements ConstraintValidator<ExistingLossReasonId, Long> {

    private final LossReasonRepository lossReasonRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        return lossReasonRepository.existsById(id);
    }
}
