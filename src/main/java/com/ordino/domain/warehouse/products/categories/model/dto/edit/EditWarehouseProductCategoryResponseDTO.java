package com.ordino.domain.warehouse.products.categories.model.dto.edit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditWarehouseProductCategoryResponseDTO {
    private Long id;
    private String category;
    private Long parentCategoryId;
}
