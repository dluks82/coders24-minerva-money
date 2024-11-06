CREATE TABLE transactions
(
    id              UUID PRIMARY KEY,
    amount          NUMERIC(18, 2)              NOT NULL,
    type            VARCHAR(20)                 NOT NULL,
    description     TEXT,
    date            DATE                        NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    deleted_at      TIMESTAMP WITHOUT TIME ZONE,
    deletion_reason TEXT,
    deleted         BOOLEAN                     NOT NULL DEFAULT FALSE,
    account_id      UUID                        NOT NULL,
    category_id     BIGINT                      NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);