package com.ordino.domain.reports.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.reports.model.dto.ExpensesReportResponseDTO;
import com.ordino.domain.reports.model.dto.InventoryLossReportResponseDTO;
import com.ordino.domain.reports.model.dto.TopOrderedProductsReportResponseDTO;
import com.ordino.domain.reports.service.ReportService;

import lombok.AllArgsConstructor;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/reports")
@Validated
@AllArgsConstructor
public class ReportsController {
    private final ReportService reportService;

    @GetMapping("/expenses")
    public ResponseEntity<ExpensesReportResponseDTO> getExpensesReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return ResponseEntity.ok().body(reportService.getExpensesReport(from, to));
    }

    @GetMapping("/inventory-loss")
    public ResponseEntity<InventoryLossReportResponseDTO> getInventoryLossReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return ResponseEntity.ok().body(reportService.getInventoryLossReport(from, to));
    }

    @GetMapping("/top-ordered-products")
    public ResponseEntity<TopOrderedProductsReportResponseDTO> getTopOrderedProductsReport(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok().body(reportService.getTopOrderedProductsReport(from, to, page, size));
    }
}
