CREATE TABLE roles (
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    role VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_roles_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (role) VALUES
    ('ADMIN'),
    ('LINE_COOK'),
    ('CHEF'),
    ('KITCHEN_STAFF'),
    ('WAREHOUSE_MANAGER'),
    ('MANAGER');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.roles TO '${appUser}'@'%';
