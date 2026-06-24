package com.ordino.domain.warehouse.warehouse_batches.validation;

import java.math.BigDecimal;

import com.ordino.core.util.PathVariablesUtil;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QuantityNotExceedsAvailableValidator implements ConstraintValidator<QuantityNotExceedsAvailable, BigDecimal> {

    private final WarehouseBatchRepository warehouseBatchRepository;
    private final HttpServletRequest request;

    @Override
    public boolean isValid(BigDecimal quantity, ConstraintValidatorContext context) {
        if (quantity == null) return true;

        Long batchId = PathVariablesUtil.extractPathId(request);
        if (batchId == null) return true;

        return warehouseBatchRepository.findById(batchId)
                .map(batch -> quantity.compareTo(batch.getQuantity()) <= 0)
                .orElse(true);
    }
}
