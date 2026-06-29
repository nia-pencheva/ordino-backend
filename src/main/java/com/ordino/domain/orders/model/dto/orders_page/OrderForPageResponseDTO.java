package com.ordino.domain.orders.model.dto.orders_page;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForPageResponseDTO {
    private Long id;
    private Instant createdAt;
}
