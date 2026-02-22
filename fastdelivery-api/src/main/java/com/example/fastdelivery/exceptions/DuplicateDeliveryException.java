package com.example.fastdelivery.exceptions;

public class DuplicateDeliveryException extends RuntimeException {
    public DuplicateDeliveryException() {
        super("Duplicate Delivery for the same order");
    }
}
