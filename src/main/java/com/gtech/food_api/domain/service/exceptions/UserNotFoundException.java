package com.gtech.food_api.domain.service.exceptions;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Long id) {
        this(String.format("Usuário com id %d não encontrado.", id));
    }
}
