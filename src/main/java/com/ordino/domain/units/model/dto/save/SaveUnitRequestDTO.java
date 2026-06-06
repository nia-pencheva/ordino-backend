package com.ordino.domain.units.model.dto.save;

import com.ordino.domain.units.validation.category.id.ValidUnitCategoryId;
import com.ordino.domain.units.validation.unit.abbreviation.ValidUnitAbbreviation;
import com.ordino.domain.units.validation.unit.name.ValidUnitName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUnitRequestDTO {
    @ValidUnitName
    private String unit;

    @ValidUnitAbbreviation
    private String abbreviation;

    @ValidUnitCategoryId
    private Long categoryId;
}
