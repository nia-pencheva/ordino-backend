package com.ordino.domain.suppliers.model.dto.products;

import java.math.BigDecimal;

import com.ordino.domain.suppliers.validation.product_id.UniqueSupplierProductId;
import com.ordino.domain.warehouse.products.validation.id.ExistingWarehouseProductId;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSupplierProductRequestDTO {
    @NotNull(message = "Warehouse product ID is required")
    @ExistingWarehouseProductId
    @UniqueSupplierProductId
    private Long warehouseProductId;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Minimum order quantity is required")
    @Positive(message = "Minimum order quantity must be positive")
    @Digits(integer = 7, fraction = 3, message = "Minimum order quantity must have at most 7 integer digits and 3 decimal places")
    private BigDecimal minOrderQuantity;
}
