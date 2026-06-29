package com.ordino.domain.units.service;

import java.util.List;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.units.model.dto.all_units.AllUnitsResponseDTO;
import com.ordino.domain.units.model.dto.edit.EditUnitCategoryResponseDTO;
import com.ordino.domain.units.model.dto.edit.EditUnitResponseDTO;
import com.ordino.domain.units.model.dto.save.SaveUnitCategoryRequestDTO;
import com.ordino.domain.units.model.dto.save.SaveUnitRequestDTO;
import com.ordino.domain.units.model.entity.Unit;

import jakarta.persistence.EntityNotFoundException;

public interface UnitService {
    AllUnitsResponseDTO getAllUnits(Integer page, Integer pageSize);

    EditUnitResponseDTO getUnitForEditing(Long id) throws EntityNotFoundException;

    void addUnit(SaveUnitRequestDTO dto);

    void saveUnit(Long id, SaveUnitRequestDTO dto) throws EntityNotFoundException;

    void deleteUnit(Long id) throws EntityNotFoundException, ForbiddenOperationException;

    List<String> unitCanBeDeleted(Unit unit);

    void addUnitCategory(SaveUnitCategoryRequestDTO dto);

    void saveUnitCategory(Long id, SaveUnitCategoryRequestDTO dto);

    void deleteUnitCategory(Long id);

    EditUnitCategoryResponseDTO getUnitCategoryForEditing(Long id);
}
