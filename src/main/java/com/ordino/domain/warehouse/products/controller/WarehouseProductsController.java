package com.ordino.domain.warehouse.products.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.warehouse.products.model.dto.WarehouseProductResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.add.AddProductToWarehouseRequestDTO;
import com.ordino.domain.warehouse.products.model.dto.addable_products_page.WarehouseProductsAddableProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.model.dto.save.SaveWarehouseProductRequestDTO;
import com.ordino.domain.warehouse.products.model.dto.warehouse_products_page.WarehouseProductsPageResponseDTO;
import com.ordino.domain.warehouse.products.service.WarehouseProductService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/warehouse-products")
@Validated
@AllArgsConstructor
public class WarehouseProductsController {
    private final WarehouseProductService warehouseProductService;

    @GetMapping()
    public ResponseEntity<WarehouseProductsPageResponseDTO> getWarehouseProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Boolean active,
        @RequestParam(required = false) @Positive Long warehouseProductCategoryId,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(warehouseProductService.getWarehouseProducts(name, active, warehouseProductCategoryId, page, pageSize));
    }

    @GetMapping("/from-approved-recipes-to-add")
    public ResponseEntity<WarehouseProductsAddableProductsPageResponseDTO> getProductsFromApprovedRecipesToAdd(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(warehouseProductService.getProductsFromApprovedRecipesToAdd(name, page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseProductResponseDTO> getWarehouseProduct(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(warehouseProductService.getWarehouseProduct(id));
    }

    @GetMapping("/addable-products")
    public ResponseEntity<WarehouseProductsAddableProductsPageResponseDTO> getAddableProducts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(warehouseProductService.getAddableProducts(name, page, pageSize));
    }

    @PostMapping("/add-product")
    public ResponseEntity<Void> addProductToWarehouse(@Valid @RequestBody AddProductToWarehouseRequestDTO dto) {
        warehouseProductService.addProductToWarehouse(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveWarehouseProduct(@PathVariable @Positive Long id, @Valid @RequestBody SaveWarehouseProductRequestDTO dto) {
        warehouseProductService.saveWarehouseProduct(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateWarehouseProduct(@PathVariable @Positive Long id) {
        warehouseProductService.deactivateWarehouseProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateWarehouseProduct(@PathVariable @Positive Long id) {
        warehouseProductService.activateWarehouseProduct(id);
        return ResponseEntity.noContent().build();
    }
}
