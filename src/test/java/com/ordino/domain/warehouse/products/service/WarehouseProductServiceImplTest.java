package com.ordino.domain.warehouse.products.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.domain.products.repository.ProductRepository;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.units.repository.UnitRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WarehouseProductServiceImplTest {

    private final WarehouseProductRepository repository = mock(WarehouseProductRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final UnitRepository unitRepository = mock(UnitRepository.class);
    private final CustomMapper mapper = new CustomMapper();

    private final WarehouseProductServiceImpl service =
            new WarehouseProductServiceImpl(repository, productRepository, unitRepository, mapper);

    @Test
    void deactivateWarehouseProduct_productInSupplierCatalog_throwsForbiddenOperationException() {
        Supplier supplier = new Supplier();
        supplier.setName("Acme Foods");

        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setSupplier(supplier);

        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setId(1L);
        warehouseProduct.setSupplierProducts(List.of(supplierProduct));

        when(repository.findById(1L)).thenReturn(Optional.of(warehouseProduct));

        assertThatThrownBy(() -> service.deactivateWarehouseProduct(1L))
                .isInstanceOf(ForbiddenOperationException.class);
    }

    @Test
    void deactivateWarehouseProduct_notInAnySupplierCatalog_succeeds() {
        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setId(2L);
        warehouseProduct.setSupplierProducts(List.of());
        warehouseProduct.setActive(true);

        when(repository.findById(2L)).thenReturn(Optional.of(warehouseProduct));

        service.deactivateWarehouseProduct(2L);

        org.assertj.core.api.Assertions.assertThat(warehouseProduct.getActive()).isFalse();
    }
}
