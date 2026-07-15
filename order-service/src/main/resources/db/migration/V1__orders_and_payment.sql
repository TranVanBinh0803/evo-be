CREATE TABLE IF NOT EXISTS t_orders (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    public_id BINARY(16),
    account_id BINARY(16),
    reservation_id BINARY(16),
    order_number VARCHAR(100),
    status VARCHAR(40) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(20) NOT NULL DEFAULT 'COD',
    payment_status VARCHAR(40) NOT NULL DEFAULT 'UNPAID',
    receiver_name VARCHAR(255),
    email VARCHAR(320),
    phone VARCHAR(30),
    address VARCHAR(1000),
    note TEXT,
    total_amount DECIMAL(19, 2) NOT NULL DEFAULT 0,
    payment_transaction_ref VARCHAR(255),
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    UNIQUE KEY uk_order_public_id (public_id),
    UNIQUE KEY uk_order_number (order_number)
);

ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS public_id BINARY(16);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS account_id BINARY(16);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS reservation_id BINARY(16);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS order_number VARCHAR(100);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS status VARCHAR(40) DEFAULT 'PENDING';
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS payment_method VARCHAR(20) DEFAULT 'COD';
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS payment_status VARCHAR(40) DEFAULT 'UNPAID';
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS receiver_name VARCHAR(255);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS email VARCHAR(320);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS phone VARCHAR(30);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS address VARCHAR(1000);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS note TEXT;
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS total_amount DECIMAL(19, 2) DEFAULT 0;
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS payment_transaction_ref VARCHAR(255);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS created_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE t_orders ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6);

UPDATE t_orders SET public_id = UUID_TO_BIN(UUID()) WHERE public_id IS NULL;
UPDATE t_orders SET order_number = CONCAT('LEGACY-', id) WHERE order_number IS NULL;
UPDATE t_orders SET status = 'PENDING' WHERE status IS NULL;
UPDATE t_orders SET payment_method = 'COD' WHERE payment_method IS NULL;
UPDATE t_orders SET payment_status = 'UNPAID' WHERE payment_status IS NULL;
UPDATE t_orders SET total_amount = 0 WHERE total_amount IS NULL;
UPDATE t_orders SET created_at = CURRENT_TIMESTAMP(6) WHERE created_at IS NULL;
UPDATE t_orders SET updated_at = CURRENT_TIMESTAMP(6) WHERE updated_at IS NULL;

CREATE TABLE IF NOT EXISTS t_order_items (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BINARY(16) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    image_url VARCHAR(1000),
    unit_price DECIMAL(19, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    line_total DECIMAL(19, 2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES t_orders(id)
);

ALTER TABLE t_order_items ADD COLUMN IF NOT EXISTS product_id BINARY(16);
ALTER TABLE t_order_items ADD COLUMN IF NOT EXISTS product_name VARCHAR(255);
ALTER TABLE t_order_items ADD COLUMN IF NOT EXISTS image_url VARCHAR(1000);
ALTER TABLE t_order_items ADD COLUMN IF NOT EXISTS unit_price DECIMAL(19, 2);
ALTER TABLE t_order_items ADD COLUMN IF NOT EXISTS quantity INTEGER;
ALTER TABLE t_order_items ADD COLUMN IF NOT EXISTS line_total DECIMAL(19, 2);
