package com.ordino.domain.recipes.logs.archive.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.recipes.logs.archive.model.dto.RecipeArchiveArchivedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.archive.model.dto.RecipeArchiveReturnedToApprovedLogEntryResponseDTO;
import com.ordino.domain.recipes.logs.archive.service.RecipeArchiveLogService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/recipes/log/archive")
@AllArgsConstructor
@Validated
public class RecipeArchiveLogController {
    private RecipeArchiveLogService archiveLogService;

    @GetMapping("/archived/{id}")
    public ResponseEntity<RecipeArchiveArchivedLogEntryResponseDTO> getArchivedLogEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(archiveLogService.getArchivedLogEntry(id));
    }

    @GetMapping("/returned-to-approved/{id}")
    public ResponseEntity<RecipeArchiveReturnedToApprovedLogEntryResponseDTO> getReturnedToApprovedEntry(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(archiveLogService.getReturnedToApprovedLogEntry(id));
    }
}
