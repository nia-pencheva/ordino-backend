package com.ordino.domain.recipes.products.categories.model.dto.products.add;

import com.ordino.domain.products.validation.id.ExistingProductId;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToRecipeIngredientCategoryRequestDTO {
    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be a positive number")
    @ExistingProductId
    private Long productId;
}
