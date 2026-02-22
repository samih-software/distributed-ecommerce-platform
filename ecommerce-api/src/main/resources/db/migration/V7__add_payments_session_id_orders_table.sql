ALTER TABLE orders
    ADD payment_session_id VARCHAR(255) NULL AFTER id;