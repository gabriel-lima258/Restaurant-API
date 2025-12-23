package com.gtech.food_api.domain.service.exceptions;

public class PermissionNotFoundException extends ResourceNotFoundException {
    public PermissionNotFoundException(String message) {
        super(message);
    }

    public PermissionNotFoundException(Long id) {
        this(String.format("Permissão com id %d não encontrada.", id));
    }
}
