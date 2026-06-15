package com.ordino.domain.recipes.model.dto.save;

import java.math.BigDecimal;

import com.ordino.domain.products.validation.id.ExistingProductId;
import com.ordino.domain.units.validation.id.ExistingUnitId;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRecipeRequestProductDTO {
    @NotNull(message = "Product ID is required")
    @ExistingProductId
    private Long productId;

    @NotNull(message = "Position is required")
    @Min(value = 1, message = "Position must be at least 1")
    private Integer position;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Digits(integer = 7, fraction = 3, message = "Quantity must have at most 7 integer digits and 3 decimal places")
    private BigDecimal quantity;

    @NotNull(message = "Unit is required")
    @ExistingUnitId
    private Long unitId;
}
