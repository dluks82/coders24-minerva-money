CREATE TABLE categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)                NOT NULL,
    description TEXT,
    active      BOOLEAN                     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id     UUID,
    FOREIGN KEY (user_id) REFERENCES users (id)
);