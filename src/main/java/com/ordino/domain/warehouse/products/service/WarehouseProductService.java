package com.ordino.domain.warehouse.products.service;

import com.ordino.domain.warehouse.products.model.dto.WarehouseProductResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.add.AddProductToWarehouseRequestDTO;
import com.ordino.domain.warehouse.products.model.dto.addable_products_page.WarehouseProductsAddableProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.save.SaveWarehouseProductRequestDTO;
import com.ordino.domain.warehouse.products.model.dto.warehouse_products_page.WarehouseProductsPageResponseDTO;

public interface WarehouseProductService {
    WarehouseProductsPageResponseDTO getWarehouseProducts(String name, Boolean active, Long warehouseProductCategoryId, Integer page, Integer pageSize);

    WarehouseProductsAddableProductsPageResponseDTO getProductsFromApprovedRecipesToAdd(String name, Integer page, Integer pageSize);

    WarehouseProductResponseDTO getWarehouseProduct(Long id);

    WarehouseProductsAddableProductsPageResponseDTO getAddableProducts(String name, Integer page, Integer pageSize);

    void addProductToWarehouse(AddProductToWarehouseRequestDTO dto);

    void saveWarehouseProduct(Long id, SaveWarehouseProductRequestDTO dto);

    void deactivateWarehouseProduct(Long id);

    void activateWarehouseProduct(Long id);
}
