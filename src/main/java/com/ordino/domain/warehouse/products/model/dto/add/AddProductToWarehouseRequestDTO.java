package com.ordino.domain.warehouse.products.model.dto.add;

import java.math.BigDecimal;

import com.ordino.domain.products.validation.id.ExistingProductId;
import com.ordino.domain.units.validation.id.ExistingUnitId;
import com.ordino.domain.warehouse.products.validation.product_id.UniqueWarehouseProductProductId;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToWarehouseRequestDTO {
    @NotNull(message = "Product ID is required")
    @ExistingProductId
    @UniqueWarehouseProductProductId
    private Long productId;

    @NotNull(message = "Unit ID is required")
    @ExistingUnitId
    private Long unitId;

    @NotNull(message = "Minimum quantity is required")
    @Positive(message = "Minimum quantity must be positive")
    @Digits(integer = 7, fraction = 3, message = "Minimum quantity must have at most 7 integer digits and 3 decimal places")
    private BigDecimal minQuantity;
}
