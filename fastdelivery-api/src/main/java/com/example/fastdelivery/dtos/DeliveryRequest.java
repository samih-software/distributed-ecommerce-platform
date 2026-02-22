package com.example.fastdelivery.dtos;

import lombok.Data;

import java.util.UUID;


@Data
public class DeliveryRequest {

    private Long orderId;

    private String recipientFirstName;
    private String recipientLastName;
    private String recipientEmail;
    private String recipientPhone;

    private String street;
    private String streetNumber;
    private String city;
    private String postalCode;
}
