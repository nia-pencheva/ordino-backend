package com.ordino.domain.orders.model.dto.create;

import java.time.Instant;
import java.util.List;

import com.ordino.core.validation.NullOrNotBlank.NullOrNotBlank;
import com.ordino.domain.orders.validation.NoDuplicateWarehouseProductIds;
import com.ordino.domain.suppliers.validation.id.ExistingActiveSupplierId;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequestDTO {
    @NotNull(message = "Supplier ID is required")
    @ExistingActiveSupplierId
    private Long supplierId;

    @Valid
    @NotEmpty(message = "At least one product is required")
    @NoDuplicateWarehouseProductIds
    private List<CreateOrderRequestProductsDTO> products;

    @NotNull(message = "Expected delivery date is required")
    @Future(message = "Expected delivery date must be in the future")
    private Instant expectedDelivery;

    @NullOrNotBlank(message = "Notes must not be blank")
    private String notes;
}
