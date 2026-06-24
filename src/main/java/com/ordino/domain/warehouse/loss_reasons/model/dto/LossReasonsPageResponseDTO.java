package com.ordino.domain.warehouse.loss_reasons.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LossReasonsPageResponseDTO {
    private List<LossReasonResponseDTO> lossReasons;
    private Long totalElements;
    private Integer totalPages;
}
