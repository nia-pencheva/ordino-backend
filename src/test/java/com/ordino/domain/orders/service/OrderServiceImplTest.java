package com.ordino.domain.orders.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.orders.model.dto.create.CreateOrderRequestDTO;
import com.ordino.domain.orders.model.dto.create.CreateOrderRequestProductsDTO;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestDTO;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestProductDTO;
import com.ordino.domain.orders.model.entity.Order;
import com.ordino.domain.orders.model.entity.OrderProduct;
import com.ordino.domain.orders.model.entity.OrderStatus;
import com.ordino.domain.orders.repository.OrderProductRepository;
import com.ordino.domain.orders.repository.OrderRepository;
import com.ordino.domain.orders.repository.OrderStatusRepository;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.suppliers.repository.SupplierProductRepository;
import com.ordino.domain.suppliers.repository.SupplierRepository;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.service.WarehouseBatchEventLogService;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;

import jakarta.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {

    private final CustomMapper mapper = new CustomMapper();
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderProductRepository orderProductRepository = mock(OrderProductRepository.class);
    private final OrderStatusRepository orderStatusRepository = mock(OrderStatusRepository.class);
    private final SupplierRepository supplierRepository = mock(SupplierRepository.class);
    private final SupplierProductRepository supplierProductRepository = mock(SupplierProductRepository.class);
    private final WarehouseProductRepository warehouseProductRepository = mock(WarehouseProductRepository.class);
    private final WarehouseBatchRepository warehouseBatchRepository = mock(WarehouseBatchRepository.class);
    private final WarehouseBatchEventRepository warehouseBatchEventRepository = mock(WarehouseBatchEventRepository.class);
    private final WarehouseBatchEventLogService logService = mock(WarehouseBatchEventLogService.class);

    private final OrderServiceImpl service = new OrderServiceImpl(
            mapper, orderRepository, orderProductRepository, orderStatusRepository, supplierRepository,
            supplierProductRepository, warehouseProductRepository, warehouseBatchRepository,
            warehouseBatchEventRepository, logService
    );

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateAs(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(new DatabaseUserDetails(user), null, List.of())
        );
    }

    private Order orderWithStatus(String statusName) {
        Order order = new Order();
        order.setId(1L);
        OrderStatus status = new OrderStatus();
        status.setStatus(statusName);
        order.setOrderStatus(status);
        return order;
    }

    @Test
    void createOrder_productNotInSuppliersCatalog_throwsEntityNotFound() {
        authenticateAs(new User());

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(orderStatusRepository.findByStatus("PENDING")).thenReturn(Optional.of(new OrderStatus()));

        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setId(5L);
        Product product = new Product();
        product.setName("Flour");
        warehouseProduct.setProduct(product);
        when(warehouseProductRepository.findById(5L)).thenReturn(Optional.of(warehouseProduct));
        when(supplierProductRepository.findBySupplierIdAndWarehouseProductId(1L, 5L)).thenReturn(Optional.empty());

        CreateOrderRequestProductsDTO productDTO = new CreateOrderRequestProductsDTO();
        productDTO.setWarehouseProductId(5L);
        productDTO.setExpectedQuantity(new BigDecimal("2.000"));

        CreateOrderRequestDTO dto = new CreateOrderRequestDTO();
        dto.setSupplierId(1L);
        dto.setExpectedDelivery(Instant.now().plusSeconds(3600));
        dto.setProducts(List.of(productDTO));

        assertThatThrownBy(() -> service.createOrder(dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void createOrder_validRequest_snapshotsCurrentSupplierPriceOntoOrderProduct() {
        authenticateAs(new User());

        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(orderStatusRepository.findByStatus("PENDING")).thenReturn(Optional.of(new OrderStatus()));

        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setId(5L);
        when(warehouseProductRepository.findById(5L)).thenReturn(Optional.of(warehouseProduct));

        SupplierProduct supplierProduct = new SupplierProduct();
        supplierProduct.setPrice(new BigDecimal("9.99"));
        when(supplierProductRepository.findBySupplierIdAndWarehouseProductId(1L, 5L)).thenReturn(Optional.of(supplierProduct));

        CreateOrderRequestProductsDTO productDTO = new CreateOrderRequestProductsDTO();
        productDTO.setWarehouseProductId(5L);
        productDTO.setExpectedQuantity(new BigDecimal("2.000"));

        CreateOrderRequestDTO dto = new CreateOrderRequestDTO();
        dto.setSupplierId(1L);
        dto.setExpectedDelivery(Instant.now().plusSeconds(3600));
        dto.setProducts(List.of(productDTO));

        service.createOrder(dto);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<OrderProduct>> captor = ArgumentCaptor.forClass(List.class);
        verify(orderProductRepository).saveAll(captor.capture());
        assertThat(captor.getValue().get(0).getPrice()).isEqualByComparingTo("9.99");
        assertThat(captor.getValue().get(0).getExpectedQuantity()).isEqualByComparingTo("2.000");
    }

    @Test
    void receiveOrder_orderProductNotFoundOnOrder_throwsEntityNotFound() {
        authenticateAs(new User());

        Order order = orderWithStatus("PENDING");
        order.setOrderProducts(List.of());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderStatusRepository.findByStatus("RECEIVED")).thenReturn(Optional.of(new OrderStatus()));
        when(warehouseBatchEventRepository.findByType("RECEIVED")).thenReturn(Optional.of(new com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent()));

        ReceiveOrderRequestProductDTO productDTO = new ReceiveOrderRequestProductDTO();
        productDTO.setWarehouseProductId(999L);
        productDTO.setReceivedQuantity(new BigDecimal("1.000"));

        ReceiveOrderRequestDTO dto = new ReceiveOrderRequestDTO();
        dto.setProducts(List.of(productDTO));

        assertThatThrownBy(() -> service.receiveOrder(1L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void receiveOrder_orderNotPending_throwsEntityNotFound() {
        Order order = orderWithStatus("RECEIVED");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        ReceiveOrderRequestDTO dto = new ReceiveOrderRequestDTO();
        dto.setProducts(List.of());

        assertThatThrownBy(() -> service.receiveOrder(1L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void cancelOrder_orderNotPending_throwsEntityNotFound() {
        authenticateAs(new User());

        Order order = orderWithStatus("CANCELLED");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> service.cancelOrder(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
