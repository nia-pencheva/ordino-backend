package com.ordino.domain.units.model.dto;

import com.ordino.domain.units.validation.abbreviation.ValidUnitAbbreviation;
import com.ordino.domain.units.validation.unit.ValidUnitName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitRequestDTO {
    @ValidUnitName
    private String unit;

    @ValidUnitAbbreviation
    private String abbreviation;
}
