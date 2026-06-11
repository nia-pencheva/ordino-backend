package com.ordino.domain.warehouse.products.categories.model.dto.move;

import com.ordino.domain.warehouse.products.categories.validation.parentid.ValidWarehouseProductCategoryParentId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveWarehouseProductCategoryRequestDTO {
    @ValidWarehouseProductCategoryParentId
    private Long parentId;
}
