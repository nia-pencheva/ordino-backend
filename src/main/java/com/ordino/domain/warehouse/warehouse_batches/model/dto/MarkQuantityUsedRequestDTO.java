package com.ordino.domain.warehouse.warehouse_batches.model.dto;

import java.math.BigDecimal;

import com.ordino.core.validation.NullOrNotBlank.NullOrNotBlank;
import com.ordino.domain.warehouse.warehouse_batches.validation.QuantityNotExceedsAvailable;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkQuantityUsedRequestDTO {
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be positive")
    @Digits(integer = 7, fraction = 3, message = "Quantity must have at most 7 integer digits and 3 decimal places")
    @QuantityNotExceedsAvailable
    private BigDecimal quantity;

    @NullOrNotBlank(message = "Notes must not be blank")
    private String notes;
}
