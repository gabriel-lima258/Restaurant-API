package com.gtech.food_api.domain.service.exceptions;

public class CityNotFoundException extends ResourceNotFoundException {
    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException(Long id) {
        this(String.format("City with id %d not found.", id));
    }
}
