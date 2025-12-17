package com.gtech.food_api.domain.service.exceptions;

public class OrderNotFoundException extends ResourceNotFoundException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(Long id) {
        this(String.format("Order with id %d not found.", id));
    }
}
