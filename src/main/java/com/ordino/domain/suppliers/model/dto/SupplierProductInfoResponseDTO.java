package com.ordino.domain.suppliers.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierProductInfoResponseDTO {
    private SupplierProductResponseDTO product;
    private String supplierName;
}
