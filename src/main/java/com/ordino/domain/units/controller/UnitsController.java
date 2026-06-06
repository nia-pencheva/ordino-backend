package com.ordino.domain.units.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.units.model.dto.all_units.AllUnitsResponseDTO;
import com.ordino.domain.units.model.dto.edit.EditUnitCategoryResponseDTO;
import com.ordino.domain.units.model.dto.edit.EditUnitResponseDTO;
import com.ordino.domain.units.model.dto.save.SaveUnitCategoryRequestDTO;
import com.ordino.domain.units.model.dto.save.SaveUnitRequestDTO;
import com.ordino.domain.units.service.UnitService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@AllArgsConstructor
@Validated
public class UnitsController {
    private UnitService unitService;

    @GetMapping("/units")
    public ResponseEntity<AllUnitsResponseDTO> getAllUnits(@RequestParam(required = false) @Positive Integer page, @RequestParam(required = false) @Positive Integer pageSize) {
        return ResponseEntity.ok().body(unitService.getAllUnits(page, pageSize));
    }
    

    @GetMapping("/unit-categories/{id}")
    public ResponseEntity<EditUnitCategoryResponseDTO> getUnitCategoryForEditing(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(unitService.getUnitCategoryForEditing(id));
    }
    
    @PostMapping("/unit-categories")
    public ResponseEntity<Void> addUnitCategory(@Valid @RequestBody SaveUnitCategoryRequestDTO dto) {
        unitService.addUnitCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unit-categories/{id}")
    public ResponseEntity<Void> saveUnitCategory(@PathVariable @Positive Long id, @Valid @RequestBody SaveUnitCategoryRequestDTO dto) {
        unitService.saveUnitCategory(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unit-categories/{id}")
    public ResponseEntity<Void> deleteUnitCategory(@PathVariable @Positive Long id) {
        unitService.deleteUnitCategory(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/units/{id}")
    public ResponseEntity<EditUnitResponseDTO> getUnitForEditing(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(unitService.getUnitForEditing(id));
    }

    @PostMapping("/units")
    public ResponseEntity<Void> addUnit(@Valid @RequestBody SaveUnitRequestDTO dto) {
        unitService.addUnit(dto);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/units/{id}")
    public ResponseEntity<Void> saveUnit(@PathVariable @Positive Long id, @Valid @RequestBody SaveUnitRequestDTO dto) {
        unitService.saveUnit(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/units/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable @Positive Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }
}
