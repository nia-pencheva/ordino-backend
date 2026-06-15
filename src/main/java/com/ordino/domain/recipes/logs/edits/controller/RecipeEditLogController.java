package com.ordino.domain.recipes.logs.edits.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.recipes.logs.edits.model.dto.RecipeEditLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.edits.service.RecipeEditLogService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/recipes/log/edit")
@AllArgsConstructor
@Validated
public class RecipeEditLogController {
    private RecipeEditLogService editLogService;

    @GetMapping("/{id}")
    public ResponseEntity<RecipeEditLogEntryResponseDTO> getLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(editLogService.getLogEntry(id));
    }
}
