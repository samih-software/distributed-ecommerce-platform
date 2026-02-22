CREATE TABLE IF NOT EXISTS delivery.deliveries_by_order (
    order_id                   bigint PRIMARY KEY,
    actual_delivery_time       timestamp,
    assigned_date              date,
    city                       text,
    created_at                 timestamp,
    delivery_person_id         uuid,
    estimated_duration_minutes int,
    latitude                   double,
    longitude                  double,
    planned_end_time           timestamp,
    planned_start_time         timestamp,
    postal_code                text,
    recipient_email            text,
    recipient_first_name       text,
    recipient_last_name        text,
    recipient_phone            text,
    scheduled_delivery_time    timestamp,
    status                     text,
    street                     text,
    street_number              text,
    updated_at                 timestamp
);

CREATE TABLE IF NOT EXISTS delivery.deliveries_pending_by_date (
   scheduled_date date,
   order_id       bigint,
   PRIMARY KEY (scheduled_date, order_id)
);