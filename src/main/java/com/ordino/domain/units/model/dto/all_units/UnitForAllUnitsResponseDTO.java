package com.ordino.domain.units.model.dto.all_units;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitForAllUnitsResponseDTO {
    private Long id;
    private String unit;
    private String abbreviation;
    private Long categoryId;
    private List<String> deleteForbiddenReasons;
}
