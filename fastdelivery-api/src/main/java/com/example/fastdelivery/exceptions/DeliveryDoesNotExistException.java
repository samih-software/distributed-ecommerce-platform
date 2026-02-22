package com.example.fastdelivery.exceptions;

public class DeliveryDoesNotExistException extends RuntimeException {
    public DeliveryDoesNotExistException() {
        super("Delivery does not exist for this order id");
    }
}
