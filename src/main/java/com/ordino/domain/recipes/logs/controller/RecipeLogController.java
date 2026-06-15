package com.ordino.domain.recipes.logs.controller;

import java.time.Instant;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.recipes.logs.model.dto.RecipeLogPageResponseDTO;
import com.ordino.domain.recipes.logs.service.RecipeLogService;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/recipes/log")
@AllArgsConstructor
@Validated
public class RecipeLogController {
    private RecipeLogService logService;

    @GetMapping("/{id}")
    public ResponseEntity<RecipeLogPageResponseDTO> getRecipeLogPage(
        @PathVariable @Positive Long id,
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam @Positive Integer pageSize,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to
    ) {
        return ResponseEntity.ok().body(logService.getRecipeLogPage(id, page, pageSize, from, to));
    }
}
