package com.ordino.domain.warehouse.loss_reasons.model.dto;

import com.ordino.domain.warehouse.loss_reasons.validation.reason.ValidLossReasonName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LossReasonRequestDTO {
    @ValidLossReasonName
    private String reason;
}
