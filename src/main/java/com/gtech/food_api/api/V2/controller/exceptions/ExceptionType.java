package com.gtech.food_api.api.V2.controller.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionType {
    RESOURCE_NOT_FOUND("resource-not-found", "Resource not found"),
    ENTITY_IN_USE("entity-in-use", "Entity in use"),
    BUSINESS_BAD_REQUEST("bad-request", "Violated constraints"),
    MESSAGE_NOT_READABLE("message-not-readable", "Message not readable"),
    INVALID_PATH_VARIABLE("invalid-path-variable", "Invalid path variable"),
    INVALID_DATA("invalid-data", "Invalid data"),
    SYSTEM_ERROR("system-error", "System error"),
    MAX_UPLOAD_SIZE_EXCEEDED("max-upload-size-exceeded", "Max upload size exceeded"),
    ACCESS_DENIED("access-denied", "Access denied");



    private String uri;
    private String title;

    ExceptionType(String path, String title) {
        this.uri = "https://foodapi.com.br/" + path;
        this.title = title;
    }
}
