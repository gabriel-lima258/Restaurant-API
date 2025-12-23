package com.gtech.food_api.domain.service.exceptions;

public class PhotoProductNotFoundException extends ResourceNotFoundException {
    public PhotoProductNotFoundException(String message) {
        super(message);
    }

    public PhotoProductNotFoundException(Long productId, Long restaurantId) {
        this(String.format("Foto do produto com id %d não encontrada no restaurante com id %d.", productId, restaurantId));
    }
}
