package com.gtech.food_api.api.controller.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) // só incluir campos não nulos
@Getter
@Builder
public class
ExceptionsDTO {
    // padrão RFC 7807
    private Integer status;
    private String type;
    private String title;
    private String detail;
}
