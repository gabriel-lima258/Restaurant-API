package com.gtech.food_api.domain.service.exceptions;

public class StateNotFoundException extends ResourceNotFoundException {
    public StateNotFoundException(String message) {
        super(message);
    }

    public StateNotFoundException(Long id) {
        this(String.format("State with id %d not found.", id));
    }
}
