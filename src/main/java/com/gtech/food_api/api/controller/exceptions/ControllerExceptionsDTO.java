package com.gtech.food_api.api.controller.exceptions;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ControllerExceptions {

    private LocalDateTime timestamp;
    private String message;
}
