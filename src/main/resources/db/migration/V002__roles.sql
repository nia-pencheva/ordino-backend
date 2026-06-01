CREATE TABLE roles (
    id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    role VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_roles_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles (role) VALUES
    ('admin'),
    ('line cook'),
    ('chef'),
    ('kitchen staff'),
    ('warehouse manager'),
    ('manager');

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.roles TO '${appUser}'@'%';
