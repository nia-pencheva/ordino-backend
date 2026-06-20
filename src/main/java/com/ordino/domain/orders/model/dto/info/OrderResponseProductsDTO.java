package com.ordino.domain.orders.model.dto.info;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseProductsDTO {
    private Long id;
    private Long warehouseProductId;
    private String name;
    private BigDecimal expectedQuantity;
    private BigDecimal receivedQuantity;
    private OrderResponseProductUnitDTO unit;
}
