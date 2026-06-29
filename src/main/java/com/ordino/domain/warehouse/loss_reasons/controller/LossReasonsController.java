package com.ordino.domain.warehouse.loss_reasons.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonRequestDTO;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonResponseDTO;
import com.ordino.domain.warehouse.loss_reasons.model.dto.LossReasonsPageResponseDTO;
import com.ordino.domain.warehouse.loss_reasons.service.LossReasonService;

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
@AllArgsConstructor
@RequestMapping("/loss-reasons")
@Validated
public class LossReasonsController {
    private LossReasonService lossReasonService;

    @GetMapping()
    public ResponseEntity<LossReasonsPageResponseDTO> getLossReasons(
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(lossReasonService.getLossReasons(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LossReasonResponseDTO> getLossReason(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(lossReasonService.getLossReason(id));
    }

    @PostMapping()
    public ResponseEntity<Void> addLossReason(@Valid @RequestBody LossReasonRequestDTO dto) {
        lossReasonService.addLossReason(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveLossReason(@PathVariable @Positive Long id, @Valid @RequestBody LossReasonRequestDTO dto) {
        lossReasonService.saveLossReason(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLossReason(@PathVariable @Positive Long id) {
        lossReasonService.deleteLossReason(id);
        return ResponseEntity.noContent().build();
    }
}
