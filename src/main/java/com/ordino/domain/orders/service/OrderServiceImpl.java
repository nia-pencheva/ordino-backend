package com.ordino.domain.orders.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ordino.core.config.mapping.CustomMapper;
import com.ordino.core.config.security.DatabaseUserDetails;
import com.ordino.domain.orders.model.dto.create.CreateOrderRequestDTO;
import com.ordino.domain.orders.model.dto.create.CreateOrderRequestProductsDTO;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseProductUnitDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseProductsDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseSupplierDTO;
import com.ordino.domain.orders.model.dto.info.OrderResponseUserDTO;
import com.ordino.domain.orders.model.dto.orders_page.OrderForPageResponseDTO;
import com.ordino.domain.orders.model.dto.orders_page.OrdersPageResponseDTO;
import com.ordino.domain.orders.model.entity.Order;
import com.ordino.domain.orders.model.entity.OrderProduct;
import com.ordino.domain.orders.model.entity.OrderStatus;
import com.ordino.domain.orders.repository.OrderProductRepository;
import com.ordino.domain.orders.repository.OrderRepository;
import com.ordino.domain.orders.repository.OrderStatusRepository;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.suppliers.model.entity.SupplierProduct;
import com.ordino.domain.suppliers.repository.SupplierProductRepository;
import com.ordino.domain.suppliers.repository.SupplierRepository;
import com.ordino.domain.orders.model.dto.receive.ReceiveOrderRequestProductDTO;
import com.ordino.domain.users.model.entity.User;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.products.repository.WarehouseProductRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.model.entity.WarehouseBatchEvent;
import com.ordino.domain.warehouse.warehouse_batches.logs.repository.WarehouseBatchEventRepository;
import com.ordino.domain.warehouse.warehouse_batches.logs.service.WarehouseBatchEventLogService;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final Integer pageSize = 10;
    private final CustomMapper mapper;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final WarehouseBatchRepository warehouseBatchRepository;
    private final WarehouseBatchEventRepository warehouseBatchEventRepository;
    private final WarehouseBatchEventLogService logService;

    public OrdersPageResponseDTO getOrders(Integer page, Integer pageSize, String from, String to, String orderStatus, String timeField) {
        if (orderStatus != null) {
            orderStatusRepository.findByStatus(orderStatus)
                    .orElseThrow(() -> new EntityNotFoundException("Order status not found"));
        }

        Integer pageNumber = page != null ? page - 1 : 0;
        Integer size = pageSize != null ? pageSize : this.pageSize;
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Instant fromInstant = from != null ? Instant.parse(from) : null;
        Instant toInstant = to != null ? Instant.parse(to) : null;

        Page<Order> ordersPage = "receivedAt".equals(timeField)
            ? orderRepository.findAllByReceivedAtBetweenAndOrderStatus(fromInstant, toInstant, orderStatus, pageRequest)
            : orderRepository.findAllByCreatedAtBetweenAndOrderStatus(fromInstant, toInstant, orderStatus, pageRequest);

        OrdersPageResponseDTO responseDTO = new OrdersPageResponseDTO();

        responseDTO.setOrders(
            ordersPage.stream()
                      .map(order -> {
                          OrderForPageResponseDTO dto = new OrderForPageResponseDTO();
                          dto.setId(order.getId());
                          dto.setCreatedAt(order.getCreatedAt());
                          return dto;
                      })
                      .toList()
        );

        responseDTO.setTotalElements(ordersPage.getTotalElements());
        responseDTO.setTotalPages(ordersPage.getTotalPages());

        return responseDTO;
    }

    @Transactional
    public void createOrder(CreateOrderRequestDTO dto) {
        User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        OrderStatus pendingStatus = orderStatusRepository.findByStatus("PENDING")
                .orElseThrow(() -> new EntityNotFoundException("Order status not found"));

        Order order = new Order();
        order.setPlacedBy(currentUser);
        order.setSupplier(supplier);
        order.setOrderStatus(pendingStatus);
        order.setExpectedDelivery(dto.getExpectedDelivery());
        order.setNotes(dto.getNotes());

        orderRepository.save(order);

        List<OrderProduct> orderProducts = dto.getProducts().stream()
                .map(productDTO -> buildOrderProduct(order, supplier, productDTO))
                .toList();

        orderProductRepository.saveAll(orderProducts);
    }

    private OrderProduct buildOrderProduct(Order order, Supplier supplier, CreateOrderRequestProductsDTO productDTO) {
        WarehouseProduct warehouseProduct = warehouseProductRepository.findById(productDTO.getWarehouseProductId())
                .orElseThrow(() -> new EntityNotFoundException("Warehouse product not found"));

        SupplierProduct supplierProduct = supplierProductRepository
                .findBySupplierIdAndWarehouseProductId(supplier.getId(), warehouseProduct.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Supplier does not carry product: " + warehouseProduct.getProduct().getName()
                ));

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrder(order);
        orderProduct.setWarehouseProduct(warehouseProduct);
        orderProduct.setPrice(supplierProduct.getPrice());
        orderProduct.setExpectedQuantity(productDTO.getExpectedQuantity());

        return orderProduct;
    }

    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(order.getId());
        responseDTO.setStatus(order.getOrderStatus().getStatus());
        responseDTO.setCreatedAt(order.getCreatedAt());
        responseDTO.setExpectedDelivery(order.getExpectedDelivery());
        responseDTO.setActualDelivery(order.getActualDelivery());
        responseDTO.setNotes(order.getNotes());

        responseDTO.setPlacedBy(mapper.map(order.getPlacedBy(), OrderResponseUserDTO.class));

        if (order.getFinalizedBy() != null) {
            responseDTO.setFinalizedBy(mapper.map(order.getFinalizedBy(), OrderResponseUserDTO.class));
        }

        responseDTO.setSupplier(mapper.map(order.getSupplier(), OrderResponseSupplierDTO.class));

        responseDTO.setProducts(
            order.getOrderProducts().stream()
                 .map(op -> {
                     OrderResponseProductsDTO productDTO = new OrderResponseProductsDTO();
                     productDTO.setId(op.getId());
                     productDTO.setWarehouseProductId(op.getWarehouseProduct().getId());
                     productDTO.setName(op.getWarehouseProduct().getProduct().getName());
                     productDTO.setExpectedQuantity(op.getExpectedQuantity());
                     productDTO.setReceivedQuantity(op.getReceivedQuantity());
                     productDTO.setUnit(mapper.map(op.getWarehouseProduct().getUnit(), OrderResponseProductUnitDTO.class));

                     return productDTO;
                 })
                 .toList()
        );

        return responseDTO;
    }

    @Transactional
    public void receiveOrder(Long id, ReceiveOrderRequestDTO dto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!"PENDING".equals(order.getOrderStatus().getStatus()))
            throw new EntityNotFoundException("Order not found");

        User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser();

        OrderStatus receivedStatus = orderStatusRepository.findByStatus("RECEIVED")
                .orElseThrow(() -> new EntityNotFoundException("Order status not found"));

        WarehouseBatchEvent receivedEvent = warehouseBatchEventRepository.findByType("RECEIVED")
                .orElseThrow(() -> new EntityNotFoundException("Warehouse batch event not found"));

        dto.getProducts().forEach((ReceiveOrderRequestProductDTO productDTO) -> {
            OrderProduct orderProduct = order.getOrderProducts().stream()
                    .filter(op -> op.getWarehouseProduct().getId().equals(productDTO.getWarehouseProductId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Order product not found"));

            orderProduct.setReceivedQuantity(productDTO.getReceivedQuantity());

            WarehouseBatch batch = new WarehouseBatch();
            batch.setWarehouseProduct(orderProduct.getWarehouseProduct());
            batch.setQuantity(productDTO.getReceivedQuantity());
            batch.setExpiryDate(productDTO.getExpiryDate() != null
                    ? productDTO.getExpiryDate()
                    : null);
            batch.setOrder(order);
            WarehouseBatch savedBatch = warehouseBatchRepository.save(batch);

            logService.createLog(currentUser, savedBatch, receivedEvent, productDTO.getReceivedQuantity(), dto.getNotes());
        });

        order.setOrderStatus(receivedStatus);
        order.setFinalizedBy(currentUser);
        order.setActualDelivery(Instant.now());
    }

    public void cancelOrder(Long id) {
        User currentUser = ((DatabaseUserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUser();

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (!"PENDING".equals(order.getOrderStatus().getStatus()))
            throw new EntityNotFoundException("Order not found");

        OrderStatus cancelledStatus = orderStatusRepository.findByStatus("CANCELLED")
                .orElseThrow(() -> new EntityNotFoundException("Order status not found"));

        order.setFinalizedBy(currentUser);
        order.setOrderStatus(cancelledStatus);

        orderRepository.save(order);
    }
}
