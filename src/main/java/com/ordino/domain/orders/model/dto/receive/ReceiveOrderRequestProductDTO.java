package com.ordino.domain.orders.model.dto.receive;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveOrderRequestProductDTO {
    @NotNull(message = "Warehouse product ID is required")
    private Long warehouseProductId;

    @NotNull(message = "Expected quantity is required")
    @PositiveOrZero(message = "Expected quantity must be positive")
    @Digits(integer = 7, fraction = 3, message = "Expected quantity must have at most 7 integer digits and 3 decimal places")
    private BigDecimal receivedQuantity;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private Instant expiryDate;
}
