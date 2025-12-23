package com.gtech.food_api.domain.service.exceptions;

public class KitchenNotFoundException extends ResourceNotFoundException {
    public KitchenNotFoundException(String message) {
        super(message);
    }

    public KitchenNotFoundException(Long id) {
        this(String.format("Cozinha com id %d não encontrada.", id));
    }
}
