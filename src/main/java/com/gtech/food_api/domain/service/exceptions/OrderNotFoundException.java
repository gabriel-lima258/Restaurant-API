package com.gtech.food_api.domain.service.exceptions;

public class OrderNotFoundException extends ResourceNotFoundException {
    public OrderNotFoundException(String code) {
        super(String.format("Order with code %s not found.", code));
    }
}
