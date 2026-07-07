package com.ordino.domain.recipes.validation;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.HandlerMapping;

import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestCategoryDTO;
import com.ordino.domain.recipes.model.dto.save.SaveRecipeRequestProductDTO;
import com.ordino.domain.recipes.products.categories.repository.RecipeIngredientCategoryRepository;
import com.ordino.domain.recipes.products.categories.validation.id.LeafRecipeIngredientCategoryValidator;
import com.ordino.domain.recipes.repository.RecipeRepository;
import com.ordino.domain.recipes.validation.categories.no_duplicate_ids.NoDuplicateCategoryIdsValidator;
import com.ordino.domain.recipes.validation.products.no_duplicate_ids.NoDuplicateProductIdsValidator;
import com.ordino.domain.recipes.validation.products.no_duplicate_positions.NoDuplicateProductPositionsValidator;
import com.ordino.domain.recipes.validation.title.UniqueRecipeTitleValidator;
import com.ordino.domain.warehouse.products.categories.repository.WarehouseProductCategoryRepository;
import com.ordino.domain.warehouse.products.categories.validation.id.LeafWarehouseProductCategoryValidator;

import jakarta.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeValidatorsTest {

    private HttpServletRequest requestWithPathId(Long id) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        if (id == null) {
            when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE)).thenReturn(null);
        } else {
            when(request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE))
                    .thenReturn(Map.of("id", id.toString()));
        }
        return request;
    }

    @Test
    void uniqueRecipeTitleValidator_titleUsedByActiveApprovedRecipe_returnsInvalid() {
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        UniqueRecipeTitleValidator validator = new UniqueRecipeTitleValidator();
        ReflectionTestUtils.setField(validator, "request", requestWithPathId(null));
        ReflectionTestUtils.setField(validator, "recipeRepository", recipeRepository);

        when(recipeRepository.existsByTitleAndActiveTrue("Carbonara")).thenReturn(true);

        assertThat(validator.isValid("Carbonara", null)).isFalse();
    }

    @Test
    void uniqueRecipeTitleValidator_titleUsedOnlyByDraftOrArchivedRecipe_returnsValid() {
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        UniqueRecipeTitleValidator validator = new UniqueRecipeTitleValidator();
        ReflectionTestUtils.setField(validator, "request", requestWithPathId(null));
        ReflectionTestUtils.setField(validator, "recipeRepository", recipeRepository);

        when(recipeRepository.existsByTitleAndActiveTrue("Carbonara")).thenReturn(false);

        assertThat(validator.isValid("Carbonara", null)).isTrue();
    }

    @Test
    void uniqueRecipeTitleValidator_sameRecipeKeepingOwnTitleOnEdit_returnsValid() {
        RecipeRepository recipeRepository = mock(RecipeRepository.class);
        UniqueRecipeTitleValidator validator = new UniqueRecipeTitleValidator();
        ReflectionTestUtils.setField(validator, "request", requestWithPathId(42L));
        ReflectionTestUtils.setField(validator, "recipeRepository", recipeRepository);

        when(recipeRepository.existsByTitleAndActiveTrueAndIdNot("Carbonara", 42L)).thenReturn(false);

        assertThat(validator.isValid("Carbonara", null)).isTrue();
    }

    @Test
    void noDuplicateProductPositionsValidator_duplicatePositionsInRequest_returnsInvalid() {
        NoDuplicateProductPositionsValidator validator = new NoDuplicateProductPositionsValidator();

        SaveRecipeRequestProductDTO first = new SaveRecipeRequestProductDTO();
        first.setPosition(1);
        SaveRecipeRequestProductDTO second = new SaveRecipeRequestProductDTO();
        second.setPosition(1);

        assertThat(validator.isValid(List.of(first, second), null)).isFalse();
    }

    @Test
    void noDuplicateProductIdsValidator_duplicateProductIdsInRequest_returnsInvalid() {
        NoDuplicateProductIdsValidator validator = new NoDuplicateProductIdsValidator();

        SaveRecipeRequestProductDTO first = new SaveRecipeRequestProductDTO();
        first.setProductId(7L);
        SaveRecipeRequestProductDTO second = new SaveRecipeRequestProductDTO();
        second.setProductId(7L);

        assertThat(validator.isValid(List.of(first, second), null)).isFalse();
    }

    @Test
    void noDuplicateCategoryIdsValidator_duplicateCategoryIdsInRequest_returnsInvalid() {
        NoDuplicateCategoryIdsValidator validator = new NoDuplicateCategoryIdsValidator();

        SaveRecipeRequestCategoryDTO first = new SaveRecipeRequestCategoryDTO();
        first.setRecipeCategoryId(3L);
        SaveRecipeRequestCategoryDTO second = new SaveRecipeRequestCategoryDTO();
        second.setRecipeCategoryId(3L);

        assertThat(validator.isValid(List.of(first, second), null)).isFalse();
    }

    @Test
    void leafRecipeIngredientCategoryValidator_categoryHasSubcategories_returnsInvalid() {
        RecipeIngredientCategoryRepository repository = mock(RecipeIngredientCategoryRepository.class);
        LeafRecipeIngredientCategoryValidator validator = new LeafRecipeIngredientCategoryValidator();
        ReflectionTestUtils.setField(validator, "repository", repository);

        when(repository.existsByParentCategoryId(1L)).thenReturn(true);

        assertThat(validator.isValid(1L, null)).isFalse();
    }

    @Test
    void leafRecipeIngredientCategoryValidator_leafCategory_returnsValid() {
        RecipeIngredientCategoryRepository repository = mock(RecipeIngredientCategoryRepository.class);
        LeafRecipeIngredientCategoryValidator validator = new LeafRecipeIngredientCategoryValidator();
        ReflectionTestUtils.setField(validator, "repository", repository);

        when(repository.existsByParentCategoryId(1L)).thenReturn(false);

        assertThat(validator.isValid(1L, null)).isTrue();
    }

    @Test
    void leafWarehouseProductCategoryValidator_categoryHasSubcategories_returnsInvalid() {
        WarehouseProductCategoryRepository repository = mock(WarehouseProductCategoryRepository.class);
        LeafWarehouseProductCategoryValidator validator = new LeafWarehouseProductCategoryValidator();
        ReflectionTestUtils.setField(validator, "repository", repository);

        when(repository.existsByParentCategoryId(9L)).thenReturn(true);

        assertThat(validator.isValid(9L, null)).isFalse();
    }
}
