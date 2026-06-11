package com.ordino.domain.warehouse.products.categories.model.dto.all_categories;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WarehouseProductCategoryForAllListResponseDTO {
    private Long id;
    private String category;
    private List<WarehouseProductCategoryForAllListResponseDTO> subCategories;
    private List<WarehouseProductCategoryProductForAllListResponseDTO> products;
}
