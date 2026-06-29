package com.ordino.domain.reports.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopOrderedProductsReportResponseDTO {

    private List<ProductOrderDataPoint> items;
    private long totalElements;
    private int totalPages;
    private int page;

    @Getter
    @AllArgsConstructor
    public static class ProductOrderDataPoint {
        private String productName;
        private long orderCount;
    }
}
