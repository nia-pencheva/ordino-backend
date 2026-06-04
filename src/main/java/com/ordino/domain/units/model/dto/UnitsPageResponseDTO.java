package com.ordino.domain.units.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitsPageResponseDTO {
    private List<UnitResponseDTO> units;
    private Long totalElements;
    private Integer totalPages;
}
