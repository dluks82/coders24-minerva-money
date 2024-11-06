CREATE TABLE categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)                NOT NULL,
    description TEXT,
    is_default  BOOLEAN                     NOT NULL DEFAULT FALSE,
    active      BOOLEAN                     NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id     UUID                        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);