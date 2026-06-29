package com.ordino.domain.recipes.products.categories.service;

import java.util.List;

import com.ordino.domain.recipes.products.categories.model.dto.all_categories.RecipeIngredientCategoryForAllListResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.edit.EditRecipeIngredientCategoryResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.move.MoveRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.RecipeIngredientCategoryAddableProductsPageResponseDTO;
import com.ordino.domain.recipes.products.categories.model.dto.products.add.AddProductToRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.dto.save.SaveRecipeIngredientCategoryRequestDTO;
import com.ordino.domain.recipes.products.categories.model.entity.RecipeIngredientCategory;

import jakarta.persistence.EntityNotFoundException;

public interface RecipeIngredientCategoryService {
    List<RecipeIngredientCategoryForAllListResponseDTO> getAllCategories();

    RecipeIngredientCategoryForAllListResponseDTO mapCategoryForAllListResponse(RecipeIngredientCategory category);

    void addCategory(SaveRecipeIngredientCategoryRequestDTO dto);

    EditRecipeIngredientCategoryResponseDTO getCategoryForEditing(Long id);

    void saveCategory(Long id, SaveRecipeIngredientCategoryRequestDTO dto) throws EntityNotFoundException;

    void moveCategory(Long id, MoveRecipeIngredientCategoryRequestDTO dto);

    RecipeIngredientCategoryAddableProductsPageResponseDTO getAddableProducts(Long id, String name, Integer page, Integer pageSize);

    void addProductToCategory(Long id, AddProductToRecipeIngredientCategoryRequestDTO dto);

    void removeProductFromCategory(Long id, Long productId);

    void deleteCategory(Long id) throws EntityNotFoundException;
} 