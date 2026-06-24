package com.ordino.domain.suppliers.model.dto.supplier_orders;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierOrderForPageResponseDTO {
    private Long id;
    private Instant createdAt;
}
