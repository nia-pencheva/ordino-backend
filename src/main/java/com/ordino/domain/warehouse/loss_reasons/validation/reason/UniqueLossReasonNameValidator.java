package com.ordino.domain.warehouse.loss_reasons.validation.reason;

import com.ordino.core.validation.UniquePropertyValidator;
import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueLossReasonNameValidator extends UniquePropertyValidator<UniqueLossReasonName> {

    private final LossReasonRepository lossReasonRepository;

    @Override
    protected boolean existsByProperty(String value) {
        return lossReasonRepository.existsByReason(value);
    }

    @Override
    protected boolean existsByPropertyAndIdNot(String value, Long excludeId) {
        return lossReasonRepository.existsByReasonAndIdNot(value, excludeId);
    }
}
