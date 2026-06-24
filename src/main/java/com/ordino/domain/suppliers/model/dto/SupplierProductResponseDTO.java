package com.ordino.domain.suppliers.model.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierProductResponseDTO {
    private Long id;
    private Long warehouseProductId;
    private String productName;
    private String unitAbbreviation;
    private BigDecimal price;
    private BigDecimal minOrderQuantity;
}
