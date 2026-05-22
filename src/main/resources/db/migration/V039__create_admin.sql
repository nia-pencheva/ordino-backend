INSERT INTO users (email, phone_number, password, password_changed_at, username, full_name) VALUES
    ('${ADMIN_EMAIL}', '${ADMIN_PHONE_NUMBER}', '${ADMIN_PASSWORD}', CURRENT_TIMESTAMP, '${ADMIN_USERNAME}', 'Admin');

INSERT INTO users_roles (user_id, role_id) VALUES
    (1, 1)
