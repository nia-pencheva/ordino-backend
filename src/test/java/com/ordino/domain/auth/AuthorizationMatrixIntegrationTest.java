package com.ordino.domain.auth;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import com.ordino.support.AbstractIntegrationTest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthorizationMatrixIntegrationTest extends AbstractIntegrationTest {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Test
    void accessProtectedEndpoint_expiredJwt_returns401TokenExpired() throws Exception {
        fixtures.chef("chef.expiredjwt");

        String expiredToken = Jwts.builder()
                .subject("chef.expiredjwt")
                .issuedAt(new Date(System.currentTimeMillis() - 7_200_000))
                .expiration(new Date(System.currentTimeMillis() - 3_600_000))
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        mockMvc.perform(get("/recipe-categories").header("Authorization", bearer(expiredToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("TOKEN_EXPIRED"));
    }

    @Test
    void unauthenticated_getUsersList_returns200DueToPermitAllUrlMatcher() throws Exception {
        mockMvc.perform(get("/users").param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticated_postUsers_returns401() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void asChef_postUsers_returns403NotAdmin() throws Exception {
        fixtures.chef("chef.usersforbidden");
        String token = loginAndGetToken("chef.usersforbidden", "Passw0rd!");

        mockMvc.perform(post("/users")
                        .header("Authorization", bearer(token))
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void asLineCook_accessProductsEndpoint_returns403() throws Exception {
        fixtures.lineCook("linecook.productsforbidden");
        String token = loginAndGetToken("linecook.productsforbidden", "Passw0rd!");

        mockMvc.perform(get("/products").header("Authorization", bearer(token)))
                .andExpect(status().isForbidden());
    }

    @Test
    void asWarehouseManager_accessProductsEndpoint_returns200() throws Exception {
        fixtures.warehouseManager("wm.productsallowed");
        String token = loginAndGetToken("wm.productsallowed", "Passw0rd!");

        mockMvc.perform(get("/products").header("Authorization", bearer(token)).param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void asKitchenStaff_getRecipeCategories_returns200() throws Exception {
        fixtures.kitchenStaff("ks.recipecats");
        String token = loginAndGetToken("ks.recipecats", "Passw0rd!");

        mockMvc.perform(get("/recipe-categories").header("Authorization", bearer(token)).param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void asManager_accessWarehouseBatchesEndpoint_returns403NotWarehouseManager() throws Exception {
        fixtures.manager("manager.warehouseforbidden");
        String token = loginAndGetToken("manager.warehouseforbidden", "Passw0rd!");

        mockMvc.perform(get("/warehouse-batches").header("Authorization", bearer(token)))
                .andExpect(status().isForbidden());
    }

    @Test
    void asWarehouseManager_accessSuppliersOrdersLossReasonsEndpoints_returns200ForAll() throws Exception {
        fixtures.warehouseManager("wm.allwarehouseendpoints");
        String token = loginAndGetToken("wm.allwarehouseendpoints", "Passw0rd!");

        mockMvc.perform(get("/suppliers").header("Authorization", bearer(token)).param("pageSize", "10"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/loss-reasons").header("Authorization", bearer(token)).param("pageSize", "10"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/orders").header("Authorization", bearer(token)).param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void anyAuthenticatedRole_accessReportsEndpoints_returns200() throws Exception {
        fixtures.chef("chef.reportsaccess");
        String token = loginAndGetToken("chef.reportsaccess", "Passw0rd!");

        mockMvc.perform(get("/reports/inventory-loss")
                        .header("Authorization", bearer(token))
                        .param("from", Instant.now().minusSeconds(86_400).toString())
                        .param("to", Instant.now().toString()))
                .andExpect(status().isOk());
    }
}
