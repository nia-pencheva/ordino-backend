package com.ordino.domain.products.model.dto;

import com.ordino.domain.products.validation.ValidProductNotes;
import com.ordino.domain.products.validation.name.ValidProductName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequestDTO {
    @ValidProductName
    private String name;

    @ValidProductNotes
    private String notes;
}
