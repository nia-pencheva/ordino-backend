package com.ordino.domain.reports.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpensesReportResponseDTO {

    private List<ExpensesDataPoint> dataPoints;

    @Getter
    @AllArgsConstructor
    public static class ExpensesDataPoint {
        private String date;
        private BigDecimal total;
    }
}
