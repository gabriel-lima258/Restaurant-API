package com.gtech.food_api.domain.service.exceptions;

public class PhotoProductNotFoundException extends ResourceNotFoundException {
    public PhotoProductNotFoundException(String message) {
        super(message);
    }

    public PhotoProductNotFoundException(Long productId, Long restaurantId) {
        this(String.format("Photo product with id %d not found with restaurant id %d.", productId, restaurantId));
    }
}
