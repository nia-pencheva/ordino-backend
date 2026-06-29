package com.ordino.domain.recipes.categories.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoriesPageResponseDTO;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoryRequestDTO;
import com.ordino.domain.recipes.categories.model.dto.RecipeCategoryResponseDTO;
import com.ordino.domain.recipes.categories.model.entity.RecipeCategory;
import com.ordino.domain.recipes.categories.repository.RecipeCategoryRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RecipeCategoryServiceImpl implements RecipeCategoryService {
    private final RecipeCategoryRepository repository;
    private final ModelMapper mapper;

    public RecipeCategoriesPageResponseDTO getRecipeCategories(Integer page, Integer pageSize) {
        Integer pageNumber = page != null ? page - 1 : 0;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("category").ascending());

        Page<RecipeCategory> categoriesPage = repository.findAll(pageRequest);

        RecipeCategoriesPageResponseDTO responseDTO = new RecipeCategoriesPageResponseDTO();

        responseDTO.setRecipeCategories(
            categoriesPage
                .stream()
                .map(category -> {
                    RecipeCategoryResponseDTO dto = mapper.map(category, RecipeCategoryResponseDTO.class);

                    dto.setRecipesCount(category.getRecipes().size());

                    return dto;
                })
                .toList()
        );

        responseDTO.setTotalElements(categoriesPage.getTotalElements());
        responseDTO.setTotalPages(categoriesPage.getTotalPages());

        return responseDTO;
    }

    public RecipeCategoryResponseDTO getRecipeCategory(Long id) throws EntityNotFoundException {
        RecipeCategory category = repository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe category not found"));

        return mapper.map(category, RecipeCategoryResponseDTO.class);
    }

    public void addRecipeCategory(RecipeCategoryRequestDTO dto) {
        repository.save(mapper.map(dto, RecipeCategory.class));
    }

    public void saveRecipeCategory(Long id, RecipeCategoryRequestDTO dto) throws EntityNotFoundException {
        RecipeCategory category = repository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe category not found"));

        mapper.map(dto, category);
        repository.save(category);
    }

    @Transactional
    public void deleteRecipeCategory(Long id) throws EntityNotFoundException, ForbiddenOperationException {
        RecipeCategory category = repository.findById(id)
                                            .orElseThrow(() -> new EntityNotFoundException("Recipe category not found"));

        repository.delete(category);
    }
}
