package com.ordino.domain.units.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.recipes.products.model.entity.RecipeProduct;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.units.model.entity.UnitCategory;
import com.ordino.domain.units.repository.UnitCategoryRepository;
import com.ordino.domain.units.repository.UnitRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UnitServiceImplTest {

    private final UnitRepository repository = mock(UnitRepository.class);
    private final UnitCategoryRepository categoryRepository = mock(UnitCategoryRepository.class);
    private final ModelMapper mapper = new ModelMapper();

    private final UnitServiceImpl service = new UnitServiceImpl(repository, categoryRepository, mapper);

    private Unit unitWithUsages(boolean usedInRecipe, boolean usedInWarehouseProduct) {
        Unit unit = new Unit();
        unit.setId(1L);
        unit.setRecipeProducts(usedInRecipe ? List.of(new RecipeProduct()) : List.of());
        unit.setWarehouseProducts(usedInWarehouseProduct ? List.of(new WarehouseProduct()) : List.of());
        return unit;
    }

    @Test
    void deleteUnit_usedInAnyRecipe_throwsForbiddenOperationException() {
        Unit unit = unitWithUsages(true, false);
        when(repository.findById(1L)).thenReturn(Optional.of(unit));

        assertThatThrownBy(() -> service.deleteUnit(1L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void deleteUnit_usedInAnyWarehouseProduct_throwsForbiddenOperationException() {
        Unit unit = unitWithUsages(false, true);
        when(repository.findById(1L)).thenReturn(Optional.of(unit));

        assertThatThrownBy(() -> service.deleteUnit(1L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void deleteUnitCategory_containsAtLeastOneUndeletableUnit_throwsForbiddenOperationException() {
        Unit undeletableUnit = unitWithUsages(true, false);

        UnitCategory category = new UnitCategory();
        category.setId(10L);
        category.setUnits(List.of(undeletableUnit));

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> service.deleteUnitCategory(10L))
                .isInstanceOf(ForbiddenOperationException.class);

        verify(categoryRepository, org.mockito.Mockito.never()).delete(category);
    }

    @Test
    void deleteUnitCategory_allUnitsDeletable_succeeds() {
        Unit deletableUnit = unitWithUsages(false, false);

        UnitCategory category = new UnitCategory();
        category.setId(11L);
        category.setUnits(List.of(deletableUnit));

        when(categoryRepository.findById(11L)).thenReturn(Optional.of(category));

        service.deleteUnitCategory(11L);

        verify(categoryRepository).delete(category);
    }
}
