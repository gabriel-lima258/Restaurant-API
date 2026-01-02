package com.gtech.food_api.api.V2.controller.exceptions;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) // only include non-null fields
@Getter
@Schema(name = "Exceptions", description = "DTO to represent exceptions")
@Builder
public class
ExceptionsDTO {
    // RFC 7807 standard
    @Schema(description = "Error status", example = "404")
    private Integer status; // error status, e.g.: 404
    @Schema(description = "Error type", example = "https://foodapi.com.br/entity-not-found")
    private String type; // error type, e.g.: "https://foodapi.com.br/entity-not-found"
    @Schema(description = "Error title", example = "Entity not found")
    private String title; // error title, e.g.: "Entity not found"
    @Schema(description = "Error detail", example = "Entity with id 1 not found")
    private String detail; // error detail, e.g.: "Entity with id 1 not found"
    @Schema(description = "Message for the end user", example = "Entity with id 1 not found")
    private String userMessage; // message for the end user, more friendly and easier to understand
    @Schema(description = "Error timestamp", example = "2025-01-01T00:00:00Z")
    private OffsetDateTime timestamp; // error timestamp, e.g.: "2025-01-01T00:00:00Z"
    @Schema(description = "List of fields that caused errors")
    private List<Field> fields; // list of fields that caused errors

    @Getter
    @Builder
    @Schema(name = "Field", description = "DTO to represent fields that caused errors")
    public static class Field {
        @Schema(description = "Name of the field that caused an error", example = "name")
        private String name; // name of the field that caused an error
        @Schema(description = "Error detail for the end user", example = "Name is required")
        private String userMessage; // error detail for the end user
    }
}
