package com.ordino.domain.products.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductsPageResponseDTO {
    private List<ProductResponseDTO> products;
    private Long totalElements;
    private Integer totalPages;
}
