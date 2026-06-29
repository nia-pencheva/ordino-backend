package com.ordino.domain.reports.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryLossReportResponseDTO {

    private List<LossDataPoint> dataPoints;

    @Getter
    @AllArgsConstructor
    public static class LossDataPoint {
        private String reason;
        private long count;
    }
}
