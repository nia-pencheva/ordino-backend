package com.ordino.domain.reports.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ordino.domain.orders.model.entity.OrderProduct;
import com.ordino.domain.orders.repository.OrderProductRepository;
import com.ordino.domain.reports.model.dto.ExpensesReportResponseDTO;
import com.ordino.domain.reports.model.dto.InventoryLossReportResponseDTO;
import com.ordino.domain.reports.model.dto.TopOrderedProductsReportResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventLogRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderProductRepository orderProductRepository;
    private final WarehouseBatchEventLogRepository warehouseBatchEventLogRepository;

    public ExpensesReportResponseDTO getExpensesReport(Instant from, Instant to) {
        List<OrderProduct> products = orderProductRepository.findByCreatedAtBetween(from, to);

        ZoneId zone = ZoneId.systemDefault();
        Map<LocalDate, BigDecimal> grouped = new TreeMap<>();

        for (OrderProduct op : products) {
            LocalDate date = op.getCreatedAt().atZone(zone).toLocalDate();
            grouped.merge(date, op.getPrice(), BigDecimal::add);
        }

        List<ExpensesReportResponseDTO.ExpensesDataPoint> dataPoints = new ArrayList<>();
        for (Map.Entry<LocalDate, BigDecimal> entry : grouped.entrySet()) {
            dataPoints.add(new ExpensesReportResponseDTO.ExpensesDataPoint(entry.getKey().toString(), entry.getValue()));
        }

        ExpensesReportResponseDTO dto = new ExpensesReportResponseDTO();
        dto.setDataPoints(dataPoints);
        return dto;
    }

    public InventoryLossReportResponseDTO getInventoryLossReport(Instant from, Instant to) {
        List<Object[]> rows = warehouseBatchEventLogRepository.findLossCountGroupedByReasonAndCreatedAtBetween(from, to);

        List<InventoryLossReportResponseDTO.LossDataPoint> dataPoints = new ArrayList<>();
        for (Object[] row : rows) {
            String reason = (String) row[0];
            long count = (Long) row[1];
            dataPoints.add(new InventoryLossReportResponseDTO.LossDataPoint(reason, count));
        }

        InventoryLossReportResponseDTO dto = new InventoryLossReportResponseDTO();
        dto.setDataPoints(dataPoints);
        return dto;
    }

    public TopOrderedProductsReportResponseDTO getTopOrderedProductsReport(Instant from, Instant to, int page, int size) {
        int clampedSize = Math.max(1, Math.min(100, size));
        Pageable pageable = PageRequest.of(Math.max(0, page), clampedSize);
        Page<Object[]> resultPage = orderProductRepository.findProductOrderFrequencyByNameAndCreatedAtBetween(from, to, pageable);

        List<TopOrderedProductsReportResponseDTO.ProductOrderDataPoint> items = new ArrayList<>();
        for (Object[] row : resultPage.getContent()) {
            String productName = (String) row[0];
            long orderCount = (Long) row[1];
            items.add(new TopOrderedProductsReportResponseDTO.ProductOrderDataPoint(productName, orderCount));
        }

        TopOrderedProductsReportResponseDTO dto = new TopOrderedProductsReportResponseDTO();
        dto.setItems(items);
        dto.setTotalElements(resultPage.getTotalElements());
        dto.setTotalPages(resultPage.getTotalPages());
        dto.setPage(resultPage.getNumber());
        return dto;
    }
}
