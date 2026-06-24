package com.ordino.domain.warehouse.warehouse_batches.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.warehouse.warehouse_batches.model.dto.MarkQuantityUsedRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.WriteOffQuantityRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batch.WarehouseBatchResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batches_page.WarehouseBatchesOfProductPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.products_page.WarehouseBatchesProductsPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.service.WarehouseBatchService;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/warehouse-batches")
@AllArgsConstructor
@Validated
public class WarehouseBatchesController {
    private final WarehouseBatchService warehouseBatchService;

    @GetMapping()
    public ResponseEntity<WarehouseBatchesProductsPageResponseDTO> getProducts(
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(warehouseBatchService.getProducts(page, pageSize));
    }

    @GetMapping("/{warehouseProductId}")
    public ResponseEntity<WarehouseBatchesOfProductPageResponseDTO> getBatchesOfProduct(
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @PathVariable @Positive Long warehouseProductId
    ) {
        return ResponseEntity.ok().body(warehouseBatchService.getBatchesOfProduct(warehouseProductId, page, pageSize));
    }
    
    @GetMapping("/{warehouseProductId}/{warehouseBatchId}")
    public ResponseEntity<WarehouseBatchResponseDTO> getBatch(@PathVariable @Positive Long warehouseProductId, @PathVariable @Positive Long warehouseBatchId) {
        return ResponseEntity.ok().body(warehouseBatchService.getBatch(warehouseProductId, warehouseBatchId));
    }

    @PostMapping("/{id}/mark-used")
    public ResponseEntity<Map<String, String>> markQuantityUsed(@PathVariable @Positive Long id, @Valid @RequestBody MarkQuantityUsedRequestDTO dto) {
        return ResponseEntity.ok(Map.of("redirect", warehouseBatchService.markQuantityUsed(id, dto)));
    }

    @PostMapping("/{id}/write-off")
    public ResponseEntity<Map<String, String>> writeOffQuantity(@PathVariable @Positive Long id, @Valid @RequestBody WriteOffQuantityRequestDTO dto) {
        return ResponseEntity.ok(Map.of("redirect", warehouseBatchService.writeOffQuantity(id, dto)));
    }
    
}
