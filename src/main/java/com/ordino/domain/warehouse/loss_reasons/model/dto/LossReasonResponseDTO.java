package com.ordino.domain.warehouse.loss_reasons.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LossReasonResponseDTO {
    private Long id;
    private String reason;
    private List<String> deleteForbiddenReasons;
}
