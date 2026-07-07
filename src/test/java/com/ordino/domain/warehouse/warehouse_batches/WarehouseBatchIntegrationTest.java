package com.ordino.domain.warehouse.warehouse_batches;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.domain.warehouse.warehouse_batches.repository.WarehouseBatchRepository;
import com.ordino.support.AbstractIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WarehouseBatchIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private WarehouseBatchRepository warehouseBatchRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Product product;
    private Unit unit;
    private WarehouseProduct warehouseProduct;
    private WarehouseBatch batch;

    @BeforeEach
    void seedData() {
        fixtures.seedWarehouseBatchEvents();
        product = fixtures.product("Sugar");
        unit = fixtures.unit("kilogram", "kg");
        warehouseProduct = fixtures.warehouseProduct(product, unit, new BigDecimal("1.000"));
        batch = fixtures.warehouseBatch(warehouseProduct, new BigDecimal("10.000"));
    }

    @Test
    void asWarehouseManager_writeOffBatchQuantity_returns200AndBatchQuantityReducedInDb() throws Exception {
        fixtures.warehouseManager("wm.writeoff");
        String token = loginAndGetToken("wm.writeoff", "Passw0rd!");

        mockMvc.perform(post("/warehouse-batches/" + batch.getId() + "/write-off")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content("{\"quantity\": 4.000}"))
                .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();
        WarehouseBatch reloaded = warehouseBatchRepository.findById(batch.getId()).orElseThrow();
        assertThat(reloaded.getQuantity()).isEqualByComparingTo("6.000");
    }

    @Test
    void asWarehouseManager_markBatchQuantityUsed_returns200AndEventLogEntryCreated() throws Exception {
        fixtures.warehouseManager("wm.markused");
        String token = loginAndGetToken("wm.markused", "Passw0rd!");

        mockMvc.perform(post("/warehouse-batches/" + batch.getId() + "/mark-used")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content("{\"quantity\": 2.000}"))
                .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();
        WarehouseBatch reloaded = warehouseBatchRepository.findById(batch.getId()).orElseThrow();
        assertThat(reloaded.getQuantity()).isEqualByComparingTo("8.000");
        assertThat(reloaded.getEventLogs()).hasSize(1);
        assertThat(reloaded.getEventLogs().get(0).getQuantityDelta()).isEqualByComparingTo("-2.000");
    }

    @ParameterizedTest
    @ValueSource(strings = {"line cook", "chef", "kitchen staff", "manager"})
    void asNonWarehouseManagerRoles_accessWarehouseBatchEndpoints_returns403(String role) throws Exception {
        String username = "nonwm_" + role.replace(" ", "_");
        fixtures.user(username, "Passw0rd!", false, role);
        String token = loginAndGetToken(username, "Passw0rd!");

        mockMvc.perform(get("/warehouse-batches").header("Authorization", bearer(token)))
                .andExpect(status().isForbidden());
    }
}
