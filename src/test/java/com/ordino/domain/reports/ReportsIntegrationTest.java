package com.ordino.domain.reports;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;
import com.ordino.domain.products.model.entity.Product;
import com.ordino.domain.units.model.entity.Unit;
import com.ordino.domain.warehouse.loss_reasons.repository.LossReasonRepository;
import com.ordino.domain.warehouse.products.model.entity.WarehouseProduct;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.LossReason;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;
import com.ordino.support.AbstractIntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReportsIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private LossReasonRepository lossReasonRepository;

    @Test
    void asWarehouseManager_getInventoryLossReportForDateRange_returns200WithAggregatedTotals() throws Exception {
        fixtures.seedWarehouseBatchEvents();
        String token = loginAndGetToken(fixtures.warehouseManager("wm.reportuser").getUsername(), "Passw0rd!");

        Product product = fixtures.product("Yeast");
        Unit unit = fixtures.unit("gram", "g");
        WarehouseProduct warehouseProduct = fixtures.warehouseProduct(product, unit, BigDecimal.ZERO);
        WarehouseBatch batch = fixtures.warehouseBatch(warehouseProduct, new BigDecimal("10.000"));

        LossReason expired = new LossReason();
        expired.setReason("Expired");
        expired = lossReasonRepository.save(expired);

        mockMvc.perform(post("/warehouse-batches/" + batch.getId() + "/write-off")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content("{\"quantity\": 3.000, \"lossReasonId\": " + expired.getId() + "}"))
                .andExpect(status().isOk());

        String response = mockMvc.perform(get("/reports/inventory-loss")
                        .header("Authorization", bearer(token))
                        .param("from", Instant.now().minusSeconds(3600).toString())
                        .param("to", Instant.now().plusSeconds(3600).toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(response);
        boolean foundExpiredReason = false;
        for (JsonNode point : json.get("dataPoints")) {
            if (point.get("reason").asText().equals("Expired")) {
                assertThat(point.get("count").asLong()).isEqualTo(1);
                foundExpiredReason = true;
            }
        }
        assertThat(foundExpiredReason).isTrue();
    }
}
