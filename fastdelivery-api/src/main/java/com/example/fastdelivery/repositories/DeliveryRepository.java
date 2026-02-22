package com.example.fastdelivery.repositories;


import com.example.fastdelivery.entities.Delivery;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.time.Instant;
import java.util.UUID;

public interface DeliveryRepository extends CassandraRepository<Delivery, Long> {

    @Query("UPDATE deliveries_by_order SET status = ?, updated_at = ? WHERE order_id = ?")
    void updateStatus(String status, Instant updatedAt, Long orderId);
}
