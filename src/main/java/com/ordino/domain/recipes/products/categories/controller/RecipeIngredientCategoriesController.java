package com.ordino.domain.recipes.products.categories.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordino.domain.recipes.products.categories.model.dto.all_categories.RecipeIngredientCategoryForAllListResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.edit.EditRecipeIngredientCategoryResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.move.MoveRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.RecipeIngredientCategoryAddableProductsPageResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.add.AddProductToRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.dto.save.SaveRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.service.RecipeIngredientCategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/recipe-ingredient-categories")
public class RecipeIngredientCategoriesController {
    private RecipeIngredientCategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<RecipeIngredientCategoryForAllListResponseDTO>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    @PostMapping()
    public ResponseEntity<Void> addCategory(@Valid @RequestBody SaveRecipeIngredientCategoryRequestDTO dto) {
        categoryService.addCategory(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditRecipeIngredientCategoryResponseDTO> getCategoryForEditing(@PathVariable @Positive Long id) {
        return ResponseEntity.ok().body(categoryService.getCategoryForEditing(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> saveCategory(@PathVariable @Positive Long id, @Valid @RequestBody SaveRecipeIngredientCategoryRequestDTO dto) {
        categoryService.saveCategory(id, dto);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/move")
    public ResponseEntity<Void> moveCategory(@PathVariable @Positive Long id, @Valid @RequestBody MoveRecipeIngredientCategoryRequestDTO dto) {
        categoryService.moveCategory(id, dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/get-addable-products")
    public ResponseEntity<RecipeIngredientCategoryAddableProductsPageResponseDTO> getAddableProducts(
        @PathVariable @Positive Long id, 
        @RequestParam(required = false) String name,
        @RequestParam(required = false) @Positive Integer page, 
        @RequestParam(required = false) @Positive Integer pageSize
    ) {
        return ResponseEntity.ok().body(categoryService.getAddableProducts(id, name, page, pageSize));
    }
    
    @PostMapping("/{id}/add-product")
    public ResponseEntity<Void> addProductToCategory(@PathVariable @Positive Long id, @Valid @RequestBody AddProductToRecipeIngredientCategoryRequestDTO dto) {
        categoryService.addProductToCategory(id, dto);
        return ResponseEntity.noContent().build();
    }
    
    
    @DeleteMapping("/{id}/remove-product/{productId}")
    public ResponseEntity<Void> removeProductFromCategory(@PathVariable @Positive Long id, @PathVariable @Positive Long productId) {
        categoryService.removeProductFromCategory(id, productId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
