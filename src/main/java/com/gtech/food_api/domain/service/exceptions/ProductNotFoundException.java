package com.gtech.food_api.domain.service.exceptions;

public class ProductNotFoundException extends ResourceNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId, Long restaurantId) {
        this(String.format("Product with id %d not found with restaurant id %d.", productId, restaurantId));
    }
}
