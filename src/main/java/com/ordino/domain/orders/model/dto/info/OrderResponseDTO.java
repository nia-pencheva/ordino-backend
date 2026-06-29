package com.ordino.domain.orders.model.dto.info;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private String status;
    private Instant createdAt;
    private OrderResponseUserDTO placedBy;
    private OrderResponseUserDTO finalizedBy;
    private OrderResponseSupplierDTO supplier;
    private Instant expectedDelivery;
    private Instant actualDelivery;
    private String notes;
    private List<OrderResponseProductsDTO> products;
}
