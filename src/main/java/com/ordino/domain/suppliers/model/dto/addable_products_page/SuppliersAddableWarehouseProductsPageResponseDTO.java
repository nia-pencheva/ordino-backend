package com.ordino.domain.suppliers.model.dto.addable_products_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuppliersAddableWarehouseProductsPageResponseDTO {
    private List<SuppliersAddableWarehouseProductResponseDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
