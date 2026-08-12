CREATE TABLE account (
    id UUID PRIMARY KEY,
    role VARCHAR(32) NOT NULL DEFAULT 'USER',
    email VARCHAR(320) NOT NULL,
    username VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_account_email UNIQUE (email),
    CONSTRAINT uq_account_username UNIQUE (username),
    CONSTRAINT ck_account_role CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE refresh_token (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES account (id) ON DELETE CASCADE,
    token_hash VARCHAR(64) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    revoked_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_refresh_token_token_hash UNIQUE (token_hash)
);

CREATE INDEX idx_refresh_token_account_id ON refresh_token (account_id);

