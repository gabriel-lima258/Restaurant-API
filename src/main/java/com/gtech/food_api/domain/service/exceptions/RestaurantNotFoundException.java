package com.gtech.food_api.domain.service.exceptions;

public class RestaurantNotFoundException extends ResourceNotFoundException {
    public RestaurantNotFoundException(String message) {
        super(message);
    }

    public RestaurantNotFoundException(Long id) {
        this(String.format("Restaurante com id %d não encontrado.", id));
    }
}
