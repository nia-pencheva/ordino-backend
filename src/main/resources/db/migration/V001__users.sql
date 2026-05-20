CREATE TABLE users (
    id                  INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    email               VARCHAR(254)  NOT NULL,
    phone_number        VARCHAR(30)   NOT NULL,
    password            VARCHAR(255)  NOT NULL,
    password_changed_at TIMESTAMP     NULL DEFAULT NULL,
    username            VARCHAR(50)   NOT NULL,
    full_name           VARCHAR(100)  NOT NULL,
    created_at          TIMESTAMP     NOT NULL,
    updated_at          TIMESTAMP     NULL DEFAULT NULL,
    deleted_at          TIMESTAMP     NULL DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_users_email (email),
    UNIQUE KEY uq_users_phone_number (phone_number),
    UNIQUE KEY uq_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.users TO '${appUser}'@'%';
