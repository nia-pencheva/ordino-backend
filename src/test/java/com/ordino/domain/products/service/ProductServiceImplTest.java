package com.ordino.domain.products.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.recipes.model.entity.Recipe;
import com.ordino.domain.recipes.model.entity.RecipeStatus;
import com.ordino.domain.recipes.products.model.entity.RecipeProduct;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceImplTest {

    private final ProductRepository repository = mock(ProductRepository.class);
    private final ModelMapper mapper = new ModelMapper();

    private final ProductServiceImpl service = new ProductServiceImpl(repository, mapper);

    private RecipeProduct recipeProductWithStatus(String statusName) {
        RecipeStatus status = new RecipeStatus();
        status.setStatus(statusName);

        Recipe recipe = new Recipe();
        recipe.setRecipeStatus(status);

        RecipeProduct recipeProduct = new RecipeProduct();
        recipeProduct.setRecipe(recipe);
        return recipeProduct;
    }

    @Test
    void deactivateProduct_usedInApprovedRecipe_throwsForbiddenOperationException() {
        Product product = new Product();
        product.setId(1L);
        product.setRecipeProducts(List.of(recipeProductWithStatus("APPROVED")));

        when(repository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.deactivateProduct(1L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void deactivateProduct_notUsedAnywhere_succeeds() {
        Product product = new Product();
        product.setId(2L);
        product.setActive(true);
        product.setRecipeProducts(List.of());

        when(repository.findById(2L)).thenReturn(Optional.of(product));

        service.deactivateProduct(2L);

        assertThat(product.getActive()).isFalse();
    }

    @Test
    void deleteProduct_usedInAnyRecipeRegardlessOfStatus_throwsForbiddenOperationException() {
        Product product = new Product();
        product.setId(3L);
        product.setRecipeProducts(List.of(recipeProductWithStatus("DRAFT")));

        when(repository.findById(3L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.deleteProduct(3L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void deleteProduct_hasAssociatedWarehouseProduct_throwsForbiddenOperationException() {
        Product product = new Product();
        product.setId(4L);
        product.setRecipeProducts(List.of());
        product.setWarehouseProduct(new WarehouseProduct());

        when(repository.findById(4L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.deleteProduct(4L))
                .isInstanceOf(ForbiddenOperationException.class);
    }
}
