package com.gtech.food_api.api.controller.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) // só incluir campos não nulos
@Getter
@Builder
public class
ExceptionsDTO {
    // padrão RFC 7807
    private Integer status; // status do erro, ex: 404
    private String type; // tipo de erro, ex: "https://foodapi.com.br/entity-not-found"
    private String title; // titulo do erro, ex: "Entity not found"
    private String detail; // detalhe do erro, ex: "Entity with id 1 not found"

    private String userMessage; // mensagem para o usuario final, mais amigavel e facil de entender
    private LocalDateTime timestamp; // timestamp do erro, ex: "2025-01-01T00:00:00Z"
}
