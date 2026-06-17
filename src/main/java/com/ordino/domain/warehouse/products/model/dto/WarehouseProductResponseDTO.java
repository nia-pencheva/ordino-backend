package com.ordino.domain.warehouse.products.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehouseProductResponseDTO {
    private Long productId;
    private String productName;
    private Long unitId;
    private String unitAbbreviation;
    private String unitCategoryName;
    private BigDecimal minQuantity;
    private boolean active;
    private List<String> deactivateForbiddenReasons;
}
