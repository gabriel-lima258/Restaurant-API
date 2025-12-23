package com.gtech.food_api.domain.service.exceptions;

public class ProductNotFoundException extends ResourceNotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long productId, Long restaurantId) {
        this(String.format("Produto com id %d não encontrado no restaurante com id %d.", productId, restaurantId));
    }
}
