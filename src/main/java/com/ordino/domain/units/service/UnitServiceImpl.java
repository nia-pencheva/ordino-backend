package com.ordino.domain.units.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.units.model.dto.all_units.AllUnitsResponseDTO;
import com.ordino.domain.units.model.dto.all_units.UnitCategoryForAllUnitsResponseDTO;
import com.ordino.domain.units.model.dto.all_units.UnitForAllUnitsResponseDTO;
import com.ordino.domain.units.model.dto.edit.EditUnitCategoryResponseDTO;
import com.ordino.domain.units.model.dto.edit.EditUnitResponseDTO;
import com.ordino.domain.units.model.dto.edit.UnitCategoryForEditUnitResponseDTO;
import com.ordino.domain.units.model.dto.save.SaveUnitCategoryRequestDTO;
import com.ordino.domain.units.model.dto.save.SaveUnitRequestDTO;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.units.model.entity.UnitCategory;
import com.ordino.domain.units.repository.UnitCategoryRepository;
import com.ordino.domain.units.repository.UnitRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository repository;
    private final UnitCategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public AllUnitsResponseDTO getAllUnits(Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<UnitCategory> unitsPage = categoryRepository.findAllWithUnitsOrderById(pageRequest);

        AllUnitsResponseDTO responseDTO = new AllUnitsResponseDTO();

        responseDTO.setUnitCategories(
            unitsPage
                .stream()
                .map(unitCategory -> {
                    UnitCategoryForAllUnitsResponseDTO unitCategoryResponseDTO = mapper.map(unitCategory, UnitCategoryForAllUnitsResponseDTO.class);

                    unitCategoryResponseDTO.setUnits(
                        unitCategory.getUnits()
                                    .stream()
                                    .map(unit -> {
                                        UnitForAllUnitsResponseDTO unitResponseDTO = mapper.map(unit, UnitForAllUnitsResponseDTO.class);

                                        unitResponseDTO.setDeleteForbiddenReasons(unitCanBeDeleted(unit));

                                        return unitResponseDTO;
                                    })
                                    .collect(Collectors.toList())
                    );

                    unitCategoryResponseDTO.setCanBeDeleted(
                        !unitCategoryResponseDTO.getUnits()
                                                .stream()  
                                                .anyMatch(unit -> !unit.getDeleteForbiddenReasons().isEmpty())
                    );

                    return unitCategoryResponseDTO;
                })
                .collect(Collectors.toList())
        );

        responseDTO.setTotalElements(unitsPage.getTotalElements());
        responseDTO.setTotalPages(unitsPage.getTotalPages());

        return responseDTO;
    }

    public EditUnitResponseDTO getUnitForEditing(Long id) throws EntityNotFoundException {
        Unit unit = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        EditUnitResponseDTO responseDTO = mapper.map(unit, EditUnitResponseDTO.class);

        responseDTO.setAllUnitCategories(
            categoryRepository.findAll()
                                .stream()
                                .map(category -> mapper.map(category, UnitCategoryForEditUnitResponseDTO.class))
                                .toList()
        );

        return responseDTO;
    }

    public void addUnit(SaveUnitRequestDTO dto) {
        Unit unit = new Unit();
        unit.setUnit(dto.getUnit());
        unit.setAbbreviation(dto.getAbbreviation());
        unit.setUnitCategory(categoryRepository.getReferenceById(dto.getCategoryId()));
        repository.save(unit);
    }

    public void saveUnit(Long id, SaveUnitRequestDTO dto) throws EntityNotFoundException {
        Unit unit = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        unit.setUnit(dto.getUnit());
        unit.setAbbreviation(dto.getAbbreviation());
        unit.setUnitCategory(categoryRepository.getReferenceById(dto.getCategoryId()));
        repository.save(unit);
    }

    @Transactional
    public void deleteUnit(Long id) throws EntityNotFoundException, ForbiddenOperationException {
        Unit unit = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        List<String> deleteForbiddenReasons = unitCanBeDeleted(unit);
        if(!deleteForbiddenReasons.isEmpty())
            throw new ForbiddenOperationException(deleteForbiddenReasons);

        repository.delete(unit);
    }

    public List<String> unitCanBeDeleted(Unit unit) {
        List<String> reasons = new ArrayList<>();

        if (!unit.getRecipeProducts().isEmpty()) {
            reasons.add("Cannot delete a unit that is used in a recipe");
        }

        if (!unit.getWarehouseProducts().isEmpty()) {
            reasons.add("Cannot delete a unit that is used in a warehouse product");
        }

        return reasons;
    }

    public void addUnitCategory(SaveUnitCategoryRequestDTO dto) {
        categoryRepository.save(mapper.map(dto, UnitCategory.class));
    }

    public void saveUnitCategory(Long id, SaveUnitCategoryRequestDTO dto) {
        UnitCategory unitCategory = categoryRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Unit category not found"));

        mapper.map(dto, unitCategory);
        categoryRepository.save(unitCategory);
    }

    @Transactional
    public void deleteUnitCategory(Long id) {
        UnitCategory unitCategory = categoryRepository.findById(id)
                                                .orElseThrow(() -> new EntityNotFoundException("Unit category not found"));

        if(unitCategory.getUnits().stream().anyMatch(unit -> !unitCanBeDeleted(unit).isEmpty())) {
            throw new ForbiddenOperationException(List.of("Cannot delete unit category that has undeletable units"));
        }

        categoryRepository.delete(unitCategory);
    }

    public EditUnitCategoryResponseDTO getUnitCategoryForEditing(Long id) {
        UnitCategory unitCategory = categoryRepository.findById(id)
                                                    .orElseThrow(() -> new EntityNotFoundException("Unit category not found"));

        return mapper.map(unitCategory, EditUnitCategoryResponseDTO.class);
    }
}
