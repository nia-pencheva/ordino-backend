package com.ordino.domain.recipes.categories.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.ordino.domain.recipes.categories.model.entity.RecipeCategory;
import com.ordino.domain.recipes.categories.repository.RecipeCategoryRepository;
import com.ordino.domain.recipes.model.entity.Recipe;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecipeCategoryServiceImplTest {

    private final RecipeCategoryRepository repository = mock(RecipeCategoryRepository.class);
    private final ModelMapper mapper = new ModelMapper();

    private final RecipeCategoryServiceImpl service = new RecipeCategoryServiceImpl(repository, mapper);

    @Test
    void deleteRecipeCategory_stillReferencedByRecipes_deletesSuccessfully() {
        RecipeCategory category = new RecipeCategory();
        category.setId(1L);
        category.setRecipes(List.of(new Recipe()));

        when(repository.findById(1L)).thenReturn(Optional.of(category));

        service.deleteRecipeCategory(1L);

        verify(repository).delete(category);
    }
}
