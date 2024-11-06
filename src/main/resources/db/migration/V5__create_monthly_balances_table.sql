CREATE TABLE monthly_balances
(
    id            BIGSERIAL PRIMARY KEY,
    year          INTEGER        NOT NULL,
    month         INTEGER        NOT NULL,
    final_balance NUMERIC(18, 2) NOT NULL DEFAULT 0.00,
    closed_at     TIMESTAMP WITHOUT TIME ZONE,
    account_id    UUID           NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts (id),
    UNIQUE (year, month, account_id)
);