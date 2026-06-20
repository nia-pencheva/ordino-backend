package com.ordino.domain.suppliers.model.dto.suppliers_page;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuppliersPageResponseDTO {
    private List<SupplierForPageResponseDTO> suppliers;
    private Long totalElements;
    private Integer totalPages;
}
