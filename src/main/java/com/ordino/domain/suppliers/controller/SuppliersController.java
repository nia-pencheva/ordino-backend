package com.ordino.domain.suppliers.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.suppliers.model.dto.SupplierProductResponseDTO;
import com.ordino.domain.suppliers.model.dto.SupplierResponseDTO;
import com.ordino.domain.suppliers.model.dto.add.AddSupplierRequestDTO;
import com.ordino.domain.suppliers.model.dto.addable_products_page.SuppliersAddableWarehouseProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.products.AddSupplierProductRequestDTO;
import com.ordino.domain.suppliers.model.dto.products.SaveSupplierProductRequestDTO;
import com.ordino.domain.suppliers.model.dto.save.SaveSupplierRequestDTO;
import com.ordino.domain.suppliers.model.dto.supplier_orders.SupplierOrdersPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.supplier_products_page.SupplierProductsPageResponseDTO;
import com.ordino.domain.suppliers.model.dto.suppliers_page.SuppliersPageResponseDTO;
import com.ordino.domain.suppliers.service.SupplierService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/suppliers")
@Validated
@AllArgsConstructor
public class SuppliersController {
    private SupplierService supplierService;

    @GetMapping()
    public ResponseEntity<SuppliersPageResponseDTO> getSuppliers(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String criteria,
        @RequestParam(required = false) Boolean active,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(supplierService.getSuppliers(search, criteria, active, page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierResponseDTO> getSupplier(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(supplierService.getSupplier(id));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<SupplierProductsPageResponseDTO> getSupplierProducts(
        @PathVariable @Positive Long id,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) Long warehouseProductCategoryId,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(supplierService.getSupplierProducts(id, name, warehouseProductCategoryId, page, pageSize));
    }
    
    @GetMapping("/{id}/orders")
    public ResponseEntity<SupplierOrdersPageResponseDTO> getSupplierOrders(
        @PathVariable @Positive Long id,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to,
        @RequestParam(required = false) String orderStatus,
        @RequestParam(required = false) String timeField
    ) {
        return ResponseEntity.ok().body(supplierService.getSupplierOrders(id, page, pageSize, from, to, orderStatus, timeField));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addSupplier(@Valid @RequestBody AddSupplierRequestDTO dto) {
        supplierService.addSupplier(dto);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<Void> saveSupplier(@PathVariable @Positive Long id, @Valid @RequestBody SaveSupplierRequestDTO dto) {
        supplierService.saveSupplier(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/products/addable")
    public ResponseEntity<SuppliersAddableWarehouseProductsPageResponseDTO> getAddableWarehouseProducts(
            @PathVariable @Positive Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        return ResponseEntity.ok().body(supplierService.getAddableWarehouseProducts(id, name, categoryId, page, pageSize));
    } 


    @PostMapping("/{id}/products")
    public ResponseEntity<Void> addSupplierProduct(@PathVariable @Positive Long id, @Valid @RequestBody AddSupplierProductRequestDTO dto) {
        supplierService.addSupplierProduct(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{supplierId}/products/{supplierProductId}")
    public ResponseEntity<SupplierProductResponseDTO> getSupplierProduct(
        @PathVariable @Positive Long supplierId,
        @PathVariable @Positive Long supplierProductId
    ) {
        return ResponseEntity.ok().body(supplierService.getSupplierProduct(supplierId, supplierProductId));
    }

    @GetMapping("/{supplierId}/products/{supplierProductId}/orders")
    public ResponseEntity<SupplierOrdersPageResponseDTO> getSupplierProductOrders(
        @PathVariable @Positive Long supplierId,
        @PathVariable @Positive Long supplierProductId,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to,
        @RequestParam(required = false) String orderStatus,
        @RequestParam(required = false) String timeField
    ) {
        return ResponseEntity.ok().body(supplierService.getSupplierProductOrders(supplierId, supplierProductId, page, pageSize, from, to, orderStatus, timeField));
    }

    @PostMapping("/{supplierId}/products/{supplierProductId}")
    public ResponseEntity<Void> saveSupplierProduct(
        @PathVariable @Positive Long supplierId, 
        @PathVariable @Positive Long supplierProductId, 
        @Valid @RequestBody SaveSupplierProductRequestDTO dto
    ) {
        supplierService.saveSupplierProduct(supplierId, supplierProductId, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{supplierId}/products/{supplierProductId}")
    public ResponseEntity<Void> deleteSupplierProduct(
        @PathVariable @Positive Long supplierId, 
        @PathVariable @Positive Long supplierProductId
    ) {
        supplierService.deleteSupplierProduct(supplierId, supplierProductId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateSupplier(@PathVariable @Positive Long id) {
        supplierService.deactivateSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateSupplier(@PathVariable @Positive Long id) {
        supplierService.activateSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable @Positive Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
