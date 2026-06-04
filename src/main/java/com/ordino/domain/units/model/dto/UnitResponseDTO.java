package com.ordino.domain.units.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitResponseDTO {
    private Long id;
    private String unit;
    private String abbreviation;
    private List<String> deleteForbiddenReasons;
}
