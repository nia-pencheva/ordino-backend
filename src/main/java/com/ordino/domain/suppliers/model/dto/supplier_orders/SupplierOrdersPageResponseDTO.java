package com.ordino.domain.suppliers.model.dto.supplier_orders;

import java.util.List;

import com.ordino.domain.orders.model.dto.OrderForPageResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierOrdersPageResponseDTO {
    private List<OrderForPageResponseDTO> orders;
    private Long totalElements;
    private Integer totalPages;
}
