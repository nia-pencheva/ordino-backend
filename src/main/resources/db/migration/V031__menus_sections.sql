CREATE TABLE menus_sections (
    id         INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    menu_id    INT UNSIGNED  NOT NULL,
    title      VARCHAR(100)  NOT NULL,
    position   INT UNSIGNED  NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uq_menus_sections_menu_title (menu_id, title),
    UNIQUE KEY uq_menus_sections_menu_position (menu_id, position),
    CONSTRAINT fk_menus_sections_menu_id FOREIGN KEY (menu_id) REFERENCES menus (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

GRANT SELECT, INSERT, UPDATE, DELETE ON ${dbName}.menus_sections TO '${appUser}'@'%';
