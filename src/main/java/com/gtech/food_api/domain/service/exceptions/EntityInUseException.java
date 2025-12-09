package com.gtech.food_api.domain.service.exceptions;

public class EntityInUseException extends BusinessException {
    public EntityInUseException(String message) {
        super(message);
    }
}
