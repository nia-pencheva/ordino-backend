package com.ordino.domain.units.model.dto.save;

import com.ordino.domain.units.validation.category.name.ValidUnitCategoryName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUnitCategoryRequestDTO {
    @ValidUnitCategoryName
    private String category;
}
