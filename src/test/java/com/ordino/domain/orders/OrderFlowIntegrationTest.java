package com.ordino.domain.orders;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ordino.domain.orders.model.entity.Order;
import com.ordino.domain.orders.repository.OrderRepository;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.suppliers.model.entity.Supplier;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;
import com.ordino.support.AbstractIntegrationTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WarehouseBatchRepository warehouseBatchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private WarehouseProduct warehouseProduct;
    private Supplier supplier;
    private String warehouseManagerToken;

    @BeforeEach
    void seedData() throws Exception {
        fixtures.seedOrderStatuses();
        fixtures.seedWarehouseBatchEvents();

        Product product = fixtures.product("Rice");
        Unit unit = fixtures.unit("kilogram", "kg");
        warehouseProduct = fixtures.warehouseProduct(product, unit, new BigDecimal("5.000"));

        supplier = fixtures.supplier("Rice Supplier Co");
        fixtures.supplierProduct(supplier, warehouseProduct, new BigDecimal("12.50"), new BigDecimal("10.000"));

        fixtures.warehouseManager("wm.orderflow");
        warehouseManagerToken = loginAndGetToken("wm.orderflow", "Passw0rd!");
    }

    private Order createPendingOrder() throws Exception {
        String body = """
                {
                  "supplierId": %d,
                  "expectedDelivery": "%s",
                  "products": [{"warehouseProductId": %d, "expectedQuantity": 20.000}]
                }
                """.formatted(supplier.getId(), Instant.now().plusSeconds(86400).toString(), warehouseProduct.getId());

        mockMvc.perform(post("/orders/create")
                        .header("Authorization", bearer(warehouseManagerToken))
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        return orderRepository.findAll().stream()
                .filter(o -> o.getSupplier().getId().equals(supplier.getId()))
                .findFirst()
                .orElseThrow();
    }

    @Test
    void asWarehouseManager_orderingAndReceivingFlow_createsWarehouseBatchAndSetsOrderReceived() throws Exception {
        Order order = createPendingOrder();

        String receiveBody = """
                {
                  "products": [{"warehouseProductId": %d, "receivedQuantity": 20.000, "expiryDate": "%s"}]
                }
                """.formatted(warehouseProduct.getId(), LocalDate.now().plusMonths(6));

        mockMvc.perform(post("/orders/" + order.getId() + "/receive")
                        .header("Authorization", bearer(warehouseManagerToken))
                        .contentType("application/json")
                        .content(receiveBody))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        Order reloaded = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(reloaded.getOrderStatus().getStatus()).isEqualTo("RECEIVED");
        assertThat(reloaded.getActualDelivery()).isNotNull();

        assertThat(warehouseBatchRepository.findByWarehouseProductIdAndQuantityGreaterThan(
                warehouseProduct.getId(), BigDecimal.ZERO, org.springframework.data.domain.PageRequest.of(0, 10)
        ).getContent()).anyMatch(batch -> batch.getQuantity().compareTo(new BigDecimal("20.000")) == 0);
    }

    @Test
    void asWarehouseManager_orderingAndCancelingFlow_setsOrderStatusCancelled() throws Exception {
        Order order = createPendingOrder();

        mockMvc.perform(post("/orders/" + order.getId() + "/cancel")
                        .header("Authorization", bearer(warehouseManagerToken)))
                .andExpect(status().isNoContent());

        entityManager.flush();
        entityManager.clear();

        Order reloaded = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(reloaded.getOrderStatus().getStatus()).isEqualTo("CANCELLED");
    }
}
