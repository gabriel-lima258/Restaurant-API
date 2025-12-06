package com.gtech.food_api.domain.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EntityInUseException extends BusinessException {
    public EntityInUseException(String message) {
        super(message);
    }
}
