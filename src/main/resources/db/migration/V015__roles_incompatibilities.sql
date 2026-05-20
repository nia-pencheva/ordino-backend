CREATE TABLE roles_incompatibilities (
    id                   INT UNSIGNED NOT NULL AUTO_INCREMENT,
    role_id              INT UNSIGNED NOT NULL,
    incompatible_role_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_roles_incompatibilities (role_id, incompatible_role_id),
    CONSTRAINT chk_roles_incompatibilities_order CHECK (role_id < incompatible_role_id),
    CONSTRAINT fk_roles_incompatibilities_role_id FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_roles_incompatibilities_incompatible_role_id FOREIGN KEY (incompatible_role_id) REFERENCES roles (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO roles_incompatibilities (role_id, incompatible_role_id)
SELECT LEAST(r1.id, r2.id), GREATEST(r1.id, r2.id)
FROM roles r1
JOIN roles r2 ON r1.role = 'LINE_COOK' AND r2.role = 'CHEF';

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.roles_incompatibilities TO '${appUser}'@'%';
