CREATE TABLE user_roles
(
    user_id UUID REFERENCES users (id) ON DELETE CASCADE,
    role_id BIGINT REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);