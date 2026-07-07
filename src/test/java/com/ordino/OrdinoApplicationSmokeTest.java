package com.ordino;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OrdinoApplicationSmokeTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void contextLoads() {
    }

    @Test
    void allTablesExist() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String[] expectedTables = {
            "USERS", "ROLES", "RECIPES", "RECIPE_STATUSES", "PRODUCTS", "UNITS",
            "WAREHOUSE_PRODUCTS", "WAREHOUSE_BATCHES", "SUPPLIERS_PRODUCTS",
            "ORDERS_PRODUCTS", "RECIPES_PRODUCTS", "ORDERS", "SUPPLIERS"
        };
        for (String table : expectedTables) {
            Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = ? AND TABLE_SCHEMA = 'PUBLIC'",
                Integer.class, table
            );
            assertThat(count).as("table %s should exist", table).isEqualTo(1);
        }
    }
}
