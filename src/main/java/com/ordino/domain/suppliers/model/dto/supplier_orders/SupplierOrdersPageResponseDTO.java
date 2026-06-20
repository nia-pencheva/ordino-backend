package com.ordino.domain.suppliers.model.dto.supplier_orders;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierOrdersPageResponseDTO {
    private List<SupplierOrderForPageResponseDTO> orders;
    private Long totalElements;
    private Integer totalPages;
}
