package com.gtech.food_api.domain.service.exceptions;

// exception para lidar com requisições inválidas das regras de negocios
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
