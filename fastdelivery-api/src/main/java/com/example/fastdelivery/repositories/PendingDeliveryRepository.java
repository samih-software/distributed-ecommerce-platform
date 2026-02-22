package com.example.fastdelivery.repositories;


import com.example.fastdelivery.entities.DeliveryPendingEntity;
import com.example.fastdelivery.entities.DeliveryPendingKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.time.LocalDate;
import java.util.List;

public interface PendingDeliveryRepository
        extends CassandraRepository<DeliveryPendingEntity, DeliveryPendingKey> {

    List<DeliveryPendingEntity> findByKeyScheduledDate(LocalDate date);


    List<DeliveryPendingEntity> findByKeyScheduledDateLessThanEqual(LocalDate date);
}