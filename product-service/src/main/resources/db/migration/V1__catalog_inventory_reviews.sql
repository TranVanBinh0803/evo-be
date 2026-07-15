CREATE TABLE IF NOT EXISTS brands (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    img_url VARCHAR(1000)
);

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(19, 2) NOT NULL,
    img_url VARCHAR(1000),
    discount INTEGER,
    quantity INTEGER,
    description TEXT,
    origin VARCHAR(255),
    average_rating DOUBLE PRECISION NOT NULL DEFAULT 0,
    review_count INTEGER NOT NULL DEFAULT 0,
    sold_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    brand_id UUID REFERENCES brands(id),
    category_id UUID REFERENCES categories(id)
);

ALTER TABLE products ADD COLUMN IF NOT EXISTS origin VARCHAR(255);
ALTER TABLE products ADD COLUMN IF NOT EXISTS average_rating DOUBLE PRECISION DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS review_count INTEGER DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS sold_count BIGINT DEFAULT 0;
ALTER TABLE products ADD COLUMN IF NOT EXISTS created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP;
UPDATE products SET origin = 'Vietnam' WHERE origin IS NULL OR BTRIM(origin) = '';
UPDATE products SET average_rating = 0 WHERE average_rating IS NULL;
UPDATE products SET review_count = 0 WHERE review_count IS NULL;
UPDATE products SET sold_count = 0 WHERE sold_count IS NULL;
UPDATE products SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
ALTER TABLE products ALTER COLUMN average_rating SET NOT NULL;
ALTER TABLE products ALTER COLUMN review_count SET NOT NULL;
ALTER TABLE products ALTER COLUMN sold_count SET NOT NULL;
ALTER TABLE products ALTER COLUMN created_at SET NOT NULL;

CREATE TABLE IF NOT EXISTS inventory_reservations (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_inventory_reservation_order
    ON inventory_reservations(order_id);

CREATE TABLE IF NOT EXISTS inventory_reservation_items (
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL REFERENCES inventory_reservations(id),
    product_id UUID NOT NULL REFERENCES products(id),
    quantity INTEGER NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    image_url VARCHAR(1000),
    unit_price NUMERIC(19, 2) NOT NULL,
    discounted_unit_price NUMERIC(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS product_reviews (
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL REFERENCES products(id),
    account_id UUID NOT NULL,
    reviewer_name VARCHAR(255),
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_product_review_account
    ON product_reviews(product_id, account_id);

CREATE TABLE IF NOT EXISTS product_sales_events (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_product_sales_order_type
    ON product_sales_events(order_id, event_type);
