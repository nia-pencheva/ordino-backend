package com.ordino.domain.suppliers.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private Boolean active;
    private List<String> deleteForbiddenReasons;
}
