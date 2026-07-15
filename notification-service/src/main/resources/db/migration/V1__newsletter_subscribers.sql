CREATE TABLE IF NOT EXISTS newsletter_subscribers (
    id UUID PRIMARY KEY,
    email VARCHAR(320) NOT NULL,
    subscribed_at TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_newsletter_subscriber_email
    ON newsletter_subscribers(LOWER(email));
