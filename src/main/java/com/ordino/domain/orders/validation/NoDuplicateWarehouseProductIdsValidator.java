package com.ordino.domain.orders.validation;

import java.util.List;
import java.util.Objects;

import com.ordino.domain.orders.model.dto.create.CreateOrderRequestProductsDTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoDuplicateWarehouseProductIdsValidator
        implements ConstraintValidator<NoDuplicateWarehouseProductIds, List<CreateOrderRequestProductsDTO>> {

    @Override
    public boolean isValid(List<CreateOrderRequestProductsDTO> products, ConstraintValidatorContext context) {
        if (products == null || products.isEmpty()) {
            return true;
        }
        long nonNullCount = products.stream()
                .map(CreateOrderRequestProductsDTO::getWarehouseProductId)
                .filter(Objects::nonNull)
                .count();
        long distinctCount = products.stream()
                .map(CreateOrderRequestProductsDTO::getWarehouseProductId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        return distinctCount == nonNullCount;
    }
}
