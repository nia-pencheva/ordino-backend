package com.ordino.domain.warehouse.products.categories.service;

import java.util.List;

import com.ordino.domain.warehouse.products.categories.model.dto.all_categories.WarehouseProductCategoryForAllListResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.edit.EditWarehouseProductCategoryResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.move.MoveWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.WarehouseProductCategoryAddableProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.add.AddProductToWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.save.SaveWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.entity.WarehouseProductCategory;

import jakarta.persistence.EntityNotFoundException;

public interface WarehouseProductCategoryService {
    List<WarehouseProductCategoryForAllListResponseDTO> getAllCategories();

    WarehouseProductCategoryForAllListResponseDTO mapCategoryForAllListResponse(WarehouseProductCategory category);

    void addCategory(SaveWarehouseProductCategoryRequestDTO dto);

    EditWarehouseProductCategoryResponseDTO getCategoryForEditing(Long id);

    void saveCategory(Long id, SaveWarehouseProductCategoryRequestDTO dto) throws EntityNotFoundException;

    void moveCategory(Long id, MoveWarehouseProductCategoryRequestDTO dto);

    WarehouseProductCategoryAddableProductsPageResponseDTO getAddableProducts(Long id, String name, Integer page, Integer pageSize);

    void addProductToCategory(Long id, AddProductToWarehouseProductCategoryRequestDTO dto);

    void removeProductFromCategory(Long id, Long productId);

    void deleteCategory(Long id) throws EntityNotFoundException;
}
