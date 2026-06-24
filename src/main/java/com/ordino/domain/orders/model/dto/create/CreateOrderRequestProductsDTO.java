package com.ordino.domain.orders.model.dto.create;

import java.math.BigDecimal;

import com.ordino.domain.warehouse.products.validation.id.ExistingWarehouseProductId;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestProductsDTO {
    @NotNull(message = "Warehouse product ID is required")
    @ExistingWarehouseProductId
    private Long warehouseProductId;

    @NotNull(message = "Expected quantity is required")
    @Positive(message = "Expected quantity must be positive")
    @Digits(integer = 7, fraction = 3, message = "Expected quantity must have at most 7 integer digits and 3 decimal places")
    private BigDecimal expectedQuantity;
}
