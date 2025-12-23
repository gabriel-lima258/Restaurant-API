package com.gtech.food_api.domain.service.exceptions;

public class GroupNotFoundException extends ResourceNotFoundException {
    public GroupNotFoundException(String message) {
        super(message);
    }

    public GroupNotFoundException(Long id) {
        this(String.format("Grupo com id %d não encontrado.", id));
    }
}
