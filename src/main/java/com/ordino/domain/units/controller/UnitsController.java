package com.ordino.domain.units.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.units.model.dto.UnitRequestDTO;
import com.ordino.domain.units.model.dto.UnitResponseDTO;
import com.ordino.domain.units.model.dto.UnitsPageResponseDTO;
import com.ordino.domain.units.service.UnitService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/units")
@AllArgsConstructor
@Validated
public class UnitsController {
    private UnitService unitService;

    @GetMapping()
    public ResponseEntity<UnitsPageResponseDTO> getUnits(@RequestParam(required = false) @Positive Integer page, @RequestParam(required = false) @Positive Integer pageSize) {
        return ResponseEntity.ok().body(unitService.getUnits(page, pageSize));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UnitResponseDTO> getUnit(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(unitService.getUnit(id));
    }

    @PostMapping()
    public ResponseEntity<Void> addUnit(@Valid @RequestBody UnitRequestDTO dto) {
        unitService.addUnit(dto);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<Void> saveUnit(@PathVariable @Positive Long id, @Valid @RequestBody UnitRequestDTO dto) {
        unitService.saveUnit(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable @Positive Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }
}
