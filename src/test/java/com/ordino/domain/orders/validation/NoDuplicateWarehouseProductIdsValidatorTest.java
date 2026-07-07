package com.ordino.domain.orders.validation;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.ordino.domain.orders.model.dto.create.CreateOrderRequestProductsDTO;

import static org.assertj.core.api.Assertions.assertThat;

class NoDuplicateWarehouseProductIdsValidatorTest {

    private final NoDuplicateWarehouseProductIdsValidator validator = new NoDuplicateWarehouseProductIdsValidator();

    @Test
    void isValid_duplicateWarehouseProductIdsInRequest_returnsInvalid() {
        CreateOrderRequestProductsDTO first = new CreateOrderRequestProductsDTO();
        first.setWarehouseProductId(1L);
        first.setExpectedQuantity(BigDecimal.ONE);
        CreateOrderRequestProductsDTO second = new CreateOrderRequestProductsDTO();
        second.setWarehouseProductId(1L);
        second.setExpectedQuantity(BigDecimal.TEN);

        assertThat(validator.isValid(List.of(first, second), null)).isFalse();
    }

    @Test
    void isValid_distinctWarehouseProductIds_returnsValid() {
        CreateOrderRequestProductsDTO first = new CreateOrderRequestProductsDTO();
        first.setWarehouseProductId(1L);
        CreateOrderRequestProductsDTO second = new CreateOrderRequestProductsDTO();
        second.setWarehouseProductId(2L);

        assertThat(validator.isValid(List.of(first, second), null)).isTrue();
    }
}
