package com.ordino.domain.recipes.categories.service;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoriesPageResponseDTO;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoryRequestDTO;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoryResponseDTO;

import jakarta.persistence.EntityNotFoundException;

public interface RecipeCategoryService {
    RecipeCategoriesPageResponseDTO getRecipeCategories(Integer page, Integer pageSize);

    RecipeCategoryResponseDTO getRecipeCategory(Long id) throws EntityNotFoundException;

    void addRecipeCategory(RecipeCategoryRequestDTO dto);

    void saveRecipeCategory(Long id, RecipeCategoryRequestDTO dto) throws EntityNotFoundException;

    void deleteRecipeCategory(Long id) throws EntityNotFoundException, ForbiddenOperationException;
}
