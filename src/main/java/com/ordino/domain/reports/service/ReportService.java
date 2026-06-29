package com.ordino.domain.reports.service;

import java.time.Instant;

import com.ordino.domain.reports.model.dto.ExpensesReportResponseDTO;
import com.ordino.domain.reports.model.dto.InventoryLossReportResponseDTO;
import com.ordino.domain.reports.model.dto.TopOrderedProductsReportResponseDTO;

public interface ReportService {
    ExpensesReportResponseDTO getExpensesReport(Instant from, Instant to);

    InventoryLossReportResponseDTO getInventoryLossReport(Instant from, Instant to);

    TopOrderedProductsReportResponseDTO getTopOrderedProductsReport(Instant from, Instant to, int page, int size);
}
