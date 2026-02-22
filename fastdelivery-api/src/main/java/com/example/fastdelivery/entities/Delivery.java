package com.example.fastdelivery.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table("deliveries_by_order")
public class Delivery {

    @PrimaryKey
    @Column("order_id")
    private Long orderId;

    @Column("recipient_first_name")
    private String recipientFirstName;

    @Column("recipient_last_name")
    private String recipientLastName;

    @Column("recipient_email")
    private String recipientEmail;

    @Column("recipient_phone")
    private String recipientPhone;

    @Column("street")
    private String street;

    @Column("street_number")
    private String streetNumber;

    @Column("city")
    private String city;

    @Column("postal_code")
    private String postalCode;

    @Column("scheduled_delivery_time")
    private Instant scheduledDeliveryTime;

    @Column("actual_delivery_time")
    private Instant actualDeliveryTime;


    @Column("status")
    private DeliveryStatus status;

    @Column("delivery_person_id")
    private UUID deliveryPersonId;

    @Column("created_at")
    private Instant createdAt;

    @Column("updated_at")
    private Instant updatedAt;

    @Column("assigned_date")
    private LocalDate assignedDate;  // day the delivery is scheduled for

    @Column("planned_start_time")
    private Instant plannedStartTime; // when courier should start

    @Column("planned_end_time")
    private Instant plannedEndTime;   // when courier should finish

    @Column("estimated_duration_minutes")
    private Integer estimatedDurationMinutes; // travel + delivery estimate


    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;


}
