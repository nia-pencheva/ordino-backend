package com.ordino.domain.products.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String notes;
    private boolean active;
    private List<String> deactivateForbiddenReasons;
    private List<String> deleteForbiddenReasons;
}
