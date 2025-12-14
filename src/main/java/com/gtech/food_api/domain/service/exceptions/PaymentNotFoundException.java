package com.gtech.food_api.domain.service.exceptions;

public class PaymentNotFoundException extends ResourceNotFoundException {
    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(Long id) {
        this(String.format("Payment method with id %d not found.", id));
    }
}
