package com.ordino.domain.recipes.categories.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.recipes.categories.model.dto.RecipeCategoriesPageResponseDTO;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoryRequestDTO;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoryResponseDTO;
import com.ordino.domain.recipes.categories.service.RecipeCategoryService;

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
@RequestMapping("/recipe-categories")
@Validated
public class RecipeCategoriesController {
    private RecipeCategoryService recipeCategoryService;

    @GetMapping()
    public ResponseEntity<RecipeCategoriesPageResponseDTO> getRecipeCategories(
        @RequestParam(required = false) @Positive Integer page,
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(recipeCategoryService.getRecipeCategories(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeCategoryResponseDTO> getRecipeCategory(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(recipeCategoryService.getRecipeCategory(id));
    }

    @PostMapping()
    public ResponseEntity<Void> addRecipeCategory(@Valid @RequestBody RecipeCategoryRequestDTO dto) {
        recipeCategoryService.addRecipeCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveRecipeCategory(@PathVariable @Positive Long id, @Valid @RequestBody RecipeCategoryRequestDTO dto) {
        recipeCategoryService.saveRecipeCategory(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipeCategory(@PathVariable @Positive Long id) {
        recipeCategoryService.deleteRecipeCategory(id);
        return ResponseEntity.noContent().build();
    }
}
