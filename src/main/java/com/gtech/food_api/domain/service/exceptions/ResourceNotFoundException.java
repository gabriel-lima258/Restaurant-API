package com.gtech.food_api.domain.service.exceptions;

public abstract class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
