package com.ordino.domain.units.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.units.model.dto.UnitRequestDTO;
import com.ordino.domain.units.model.dto.UnitResponseDTO;
import com.ordino.domain.units.model.dto.UnitsPageResponseDTO;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.units.repository.UnitRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UnitService {
    private final UnitRepository repository;
    private final ModelMapper mapper;

    public UnitsPageResponseDTO getUnits(Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Unit> unitsPage = repository.findAll(pageRequest);
        
        UnitsPageResponseDTO responseDTO = new UnitsPageResponseDTO();

        responseDTO.setUnits(
            unitsPage
                .stream()
                .map(unit -> {
                    UnitResponseDTO dto = mapper.map(unit, UnitResponseDTO.class);

                    dto.setDeleteForbiddenReasons(canBeDeleted(unit));

                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(unitsPage.getTotalElements());
        responseDTO.setTotalPages(unitsPage.getTotalPages());

        return responseDTO;
    }

    public UnitResponseDTO getUnit(Long id) throws EntityNotFoundException {
        Unit unit = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        return mapper.map(unit, UnitResponseDTO.class);
    }

    public void addUnit(UnitRequestDTO dto) {
        repository.save(mapper.map(dto, Unit.class));
    }

    public void saveUnit(Long id, UnitRequestDTO dto) throws EntityNotFoundException {
        Unit unit = repository.findById(id)
                                    .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        mapper.map(dto, unit);
        repository.save(unit);
    }

    @Transactional
    public void deleteUnit(Long id) throws EntityNotFoundException, ForbiddenOperationException {
        Unit unit = repository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException("Unit not found"));

        List<String> deleteForbiddenReasons = canBeDeleted(unit);
        if(!deleteForbiddenReasons.isEmpty())
            throw new ForbiddenOperationException(deleteForbiddenReasons);

        repository.delete(unit);
    }

    private List<String> canBeDeleted(Unit unit) {
        List<String> reasons = new ArrayList<>();

        if (!unit.getRecipeProducts().isEmpty()) {
            reasons.add("Cannot delete a unit that is used in a recipe");
        }

        if (!unit.getWarehouseProducts().isEmpty()) {
            reasons.add("Cannot delete a unit that is used in a warehouse product");
        }

        return reasons;
    }
}
