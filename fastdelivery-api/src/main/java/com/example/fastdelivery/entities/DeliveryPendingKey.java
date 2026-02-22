package com.example.fastdelivery.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.time.LocalDate;
import java.util.UUID;


@Data
@PrimaryKeyClass
public class DeliveryPendingKey {

    @PrimaryKeyColumn(name = "scheduled_date", type = PrimaryKeyType.PARTITIONED)
    private LocalDate scheduledDate;

    @PrimaryKeyColumn(name = "order_id", type = PrimaryKeyType.CLUSTERED)
    private Long orderId;

}
