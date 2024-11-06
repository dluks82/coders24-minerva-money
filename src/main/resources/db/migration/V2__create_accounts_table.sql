CREATE TABLE accounts
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255)                NOT NULL,
    current_balance NUMERIC(18, 2)              NOT NULL DEFAULT 0.00,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id         UUID                        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);