package com.ordino.domain.warehouse.warehouse_batches.logs.controller;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.entry.WarehouseBatchEventLogEntryResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.dto.page.WarehouseBatchEventLogPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.service.WarehouseBatchEventLogService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/warehouse-batches/log")
@Validated
@AllArgsConstructor
public class WarehouseBatchEventLogController {
    private final WarehouseBatchEventLogService logService;

    @GetMapping()
    public ResponseEntity<WarehouseBatchEventLogPageResponseDTO> getWarehouseBatchLog(
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize,
        @RequestParam(required = false) Long warehouseProductId,
        @RequestParam(required = false) Long warehouseProductCategoryId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to

    ) {
        return ResponseEntity.ok().body(logService.getWarehouseBatchLog(page, pageSize, warehouseProductId, warehouseProductCategoryId, from, to));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseBatchEventLogEntryResponseDTO> getWarehouseBatchLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(logService.getWarehouseBatchLogEntry(id));
    }
}
