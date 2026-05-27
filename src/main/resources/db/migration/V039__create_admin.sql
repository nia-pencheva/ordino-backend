INSERT INTO users (email, phone_number, password, password_changed_at, username, full_name) VALUES
    ('${adminEmail}', '${adminPhoneNumber}', '${adminPassword}', CURRENT_TIMESTAMP, '${adminUsername}', 'Admin');

INSERT INTO users_roles (user_id, role_id) VALUES
    (1, 1)
