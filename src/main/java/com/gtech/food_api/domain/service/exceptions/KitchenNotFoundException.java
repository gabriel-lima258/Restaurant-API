package com.gtech.food_api.domain.service.exceptions;

public class KitchenNotFoundException extends RuntimeException {
    public KitchenNotFoundException(String message) {
        super(message);
    }
}
