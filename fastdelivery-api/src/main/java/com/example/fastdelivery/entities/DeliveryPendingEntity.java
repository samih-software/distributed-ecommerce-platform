package com.example.fastdelivery.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("deliveries_pending_by_date")
public class DeliveryPendingEntity {

    @PrimaryKey
    private DeliveryPendingKey key;

}