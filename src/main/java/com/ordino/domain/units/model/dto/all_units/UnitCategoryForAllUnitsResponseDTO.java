package com.ordino.domain.units.model.dto.all_units;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitCategoryForAllUnitsResponseDTO {
    private Long id;
    private String category;
    private List<UnitForAllUnitsResponseDTO> units;
    private Boolean canBeDeleted;
}
