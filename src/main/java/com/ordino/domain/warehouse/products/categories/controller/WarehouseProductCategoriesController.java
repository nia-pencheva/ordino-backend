package com.ordino.domain.warehouse.products.categories.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.warehouse.products.categories.validation.id.LeafWarehouseProductCategory;
import com.ordino.domain.warehouse.products.categories.model.dto.all_categories.WarehouseProductCategoryForAllListResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.edit.EditWarehouseProductCategoryResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.move.MoveWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.WarehouseProductCategoryAddableProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.products.add.AddProductToWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.model.dto.save.SaveWarehouseProductCategoryRequestDTO;
import com.ordino.domain.warehouse.products.categories.service.WarehouseProductCategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/warehouse-product-categories")
public class WarehouseProductCategoriesController {
    private WarehouseProductCategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<WarehouseProductCategoryForAllListResponseDTO>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    @PostMapping()
    public ResponseEntity<Void> addCategory(@Valid @RequestBody SaveWarehouseProductCategoryRequestDTO dto) {
        categoryService.addCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditWarehouseProductCategoryResponseDTO> getCategoryForEditing(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(categoryService.getCategoryForEditing(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveCategory(@PathVariable @Positive Long id, @Valid @RequestBody SaveWarehouseProductCategoryRequestDTO dto) {
        categoryService.saveCategory(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<Void> moveCategory(@PathVariable @Positive Long id, @Valid @RequestBody MoveWarehouseProductCategoryRequestDTO dto) {
        categoryService.moveCategory(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/get-addable-products")
    public ResponseEntity<WarehouseProductCategoryAddableProductsPageResponseDTO> getAddableProducts(
        @PathVariable @Positive Long id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(categoryService.getAddableProducts(id, name, page, pageSize));
    }

    @PostMapping("/{id}/add-product")
    public ResponseEntity<Void> addProductToCategory(@PathVariable @Positive @LeafWarehouseProductCategory Long id, @Valid @RequestBody AddProductToWarehouseProductCategoryRequestDTO dto) {
        categoryService.addProductToCategory(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/remove-product/{productId}")
    public ResponseEntity<Void> removeProductFromCategory(@PathVariable @Positive Long id, @PathVariable @Positive Long productId) {
        categoryService.removeProductFromCategory(id, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
