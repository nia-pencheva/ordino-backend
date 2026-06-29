package com.ordino.domain.orders.model.dto.orders_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersPageResponseDTO {
    private List<OrderForPageResponseDTO> orders;
    private Long totalElements;
    private Integer totalPages;
}
