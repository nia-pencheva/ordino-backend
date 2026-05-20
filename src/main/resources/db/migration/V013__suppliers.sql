CREATE TABLE suppliers (
    id           INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name         VARCHAR(200)  NOT NULL,
    address      VARCHAR(500)  NOT NULL,
    email        VARCHAR(254)  NOT NULL,
    phone_number VARCHAR(30)   NOT NULL,
    active       BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP     NOT NULL,
    updated_at   TIMESTAMP     NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_suppliers_name (name),
    UNIQUE KEY uq_suppliers_email (email),
    UNIQUE KEY uq_suppliers_phone_number (phone_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.suppliers TO '${appUser}'@'%';
