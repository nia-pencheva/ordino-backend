package com.ordino.domain.suppliers.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.exception.ForbiddenOperationException;
import com.ordino.core.exception.ValidationException;
import com.ordino.domain.orders.model.entity.Order;
import com.ordino.domain.orders.repository.OrderRepository;
import com.ordino.domain.orders.repository.OrderStatusRepository;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.suppliers.repository.SupplierProductRepository;
import com.ordino.domain.suppliers.repository.SupplierRepository;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SupplierServiceImplTest {

    private final SupplierRepository supplierRepository = mock(SupplierRepository.class);
    private final SupplierProductRepository supplierProductRepository = mock(SupplierProductRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderStatusRepository orderStatusRepository = mock(OrderStatusRepository.class);
    private final WarehouseProductRepository warehouseProductRepository = mock(WarehouseProductRepository.class);
    private final CustomMapper mapper = new CustomMapper();

    private final SupplierServiceImpl service = new SupplierServiceImpl(
            supplierRepository, supplierProductRepository, orderRepository, orderStatusRepository,
            warehouseProductRepository, mapper
    );

    @Test
    void deleteSupplier_hasExistingOrders_throwsForbiddenOperationException() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setOrders(List.of(new Order()));

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        assertThatThrownBy(() -> service.deleteSupplier(1L))
                .isInstanceOf(ForbiddenOperationException.class);

        verify(supplierRepository, never()).delete(supplier);
    }

    @Test
    void deleteSupplier_noAssociatedOrders_deletesSuccessfully() {
        Supplier supplier = new Supplier();
        supplier.setId(2L);
        supplier.setOrders(List.of());

        when(supplierRepository.findById(2L)).thenReturn(Optional.of(supplier));

        service.deleteSupplier(2L);

        verify(supplierRepository).delete(supplier);
    }

    @Test
    void getSuppliers_invalidSearchCriteriaParameter_throwsValidationException() {
        assertThatThrownBy(() -> service.getSuppliers("acme", "not-a-real-criteria", true, 1, 10))
                .isInstanceOf(ValidationException.class)
                .satisfies(e -> assertThat(((ValidationException) e).getField()).isEqualTo("criteria"));
    }

    @Test
    void deleteSupplierProduct_notBelongingToGivenSupplier_throwsEntityNotFound() {
        Supplier actualSupplier = new Supplier();
        actualSupplier.setId(1L);

        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setId(10L);
        supplierProduct.setSupplier(actualSupplier);

        when(supplierProductRepository.findById(10L)).thenReturn(Optional.of(supplierProduct));

        assertThatThrownBy(() -> service.deleteSupplierProduct(999L, 10L))
                .isInstanceOf(jakarta.persistence.EntityNotFoundException.class);
    }
}
