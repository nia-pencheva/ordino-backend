package com.ordino.domain.units.model.dto.all_units;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllUnitsResponseDTO {
    private List<UnitCategoryForAllUnitsResponseDTO> unitCategories;
    private Long totalElements;
    private Integer totalPages;
}
