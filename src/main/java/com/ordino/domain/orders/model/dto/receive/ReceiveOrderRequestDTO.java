package com.ordino.domain.orders.model.dto.receive;

import java.util.List;

import com.ordino.core.validation.NullOrNotBlank.NullOrNotBlank;
import com.ordino.domain.orders.validation.AllOrderProductsReceived;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveOrderRequestDTO {
    @Valid
    @AllOrderProductsReceived
    private List<ReceiveOrderRequestProductDTO> products;

    @NullOrNotBlank(message = "Notes must not be blank")
    private String notes;
}
