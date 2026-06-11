package com.ordino.domain.warehouse.products.categories.model.dto.save;

import com.ordino.domain.warehouse.products.categories.validation.name.ValidWarehouseProductCategoryName;
import com.ordino.domain.warehouse.products.categories.validation.parentid.ValidWarehouseProductCategoryParentId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveWarehouseProductCategoryRequestDTO {
    @ValidWarehouseProductCategoryName
    private String category;

    @ValidWarehouseProductCategoryParentId
    private Long parentId;
}
