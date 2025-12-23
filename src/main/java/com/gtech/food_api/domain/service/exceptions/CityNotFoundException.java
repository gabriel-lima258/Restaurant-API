package com.gtech.food_api.domain.service.exceptions;

public class CityNotFoundException extends ResourceNotFoundException {
    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException(Long id) {
        this(String.format("Cidade com id %d não encontrada.", id));
    }
}
