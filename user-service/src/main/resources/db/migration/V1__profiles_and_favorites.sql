CREATE TABLE IF NOT EXISTS user_profiles (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_profile_account ON user_profiles(account_id);
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_profile_email ON user_profiles(email);

CREATE TABLE IF NOT EXISTS user_favorites (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    product_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_favorite_product
    ON user_favorites(account_id, product_id);
