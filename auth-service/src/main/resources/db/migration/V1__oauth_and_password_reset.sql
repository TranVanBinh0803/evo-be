CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    user_role VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    token_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ
);
CREATE UNIQUE INDEX IF NOT EXISTS idx_password_reset_hash
    ON password_reset_tokens(token_hash);

CREATE TABLE IF NOT EXISTS oauth_accounts (
    id UUID PRIMARY KEY,
    provider VARCHAR(30) NOT NULL,
    provider_subject VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_oauth_provider_subject
    ON oauth_accounts(provider, provider_subject);

CREATE TABLE IF NOT EXISTS oauth_login_codes (
    id UUID PRIMARY KEY,
    code_hash VARCHAR(255) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    user_id UUID NOT NULL REFERENCES users(id)
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_oauth_login_code_hash
    ON oauth_login_codes(code_hash);
