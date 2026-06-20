package com.ordino.domain.suppliers.model.dto.supplier_products_page;

import java.util.List;

import com.ordino.domain.suppliers.model.dto.SupplierProductResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierProductsPageResponseDTO {
    private List<SupplierProductResponseDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
