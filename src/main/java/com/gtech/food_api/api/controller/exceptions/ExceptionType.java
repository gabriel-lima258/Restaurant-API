package com.gtech.food_api.api.controller.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionType {
    ENTITY_NOT_FOUND("resource-not-found", "Resource not found"),
    ENTITY_IN_USE("entity-in-use", "Entity in use"),
    BUSINESS_BAD_REQUEST("bad-request", "Violated constraints"),
    MESSAGE_NOT_READABLE("message-not-readable", "Message not readable");

    private String uri;
    private String title;

    ExceptionType(String path, String title) {
        this.uri = "https://foodapi.com.br/" + path;
        this.title = title;
    }
}
