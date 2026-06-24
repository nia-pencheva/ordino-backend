CREATE TABLE orders (
    id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    placed_by         INT UNSIGNED    NOT NULL,
    finalized_by      INT UNSIGNED    NULL,
    supplier_id       INT UNSIGNED    NOT NULL,
    expected_delivery TIMESTAMP       NOT NULL,
    actual_delivery   TIMESTAMP       NULL,
    notes             TEXT            NULL,
    order_status_id   INT UNSIGNED    NOT NULL,
    created_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_placed_by FOREIGN KEY (placed_by) REFERENCES users (id),
    CONSTRAINT fk_orders_finalized_by FOREIGN KEY (finalized_by) REFERENCES users (id),
    CONSTRAINT fk_orders_supplier_id FOREIGN KEY (supplier_id) REFERENCES suppliers (id),
    CONSTRAINT fk_orders_order_status_id FOREIGN KEY (order_status_id) REFERENCES order_statuses (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE ON ${dbName}.orders TO '${appUser}'@'%';
