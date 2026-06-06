package com.ordino.domain.units.model.dto.edit;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUnitResponseDTO {
    private Long id;
    private String unit;
    private String abbreviation;
    private Long categoryId;
    private List<UnitCategoryForEditUnitResponseDTO> allUnitCategories;
}
