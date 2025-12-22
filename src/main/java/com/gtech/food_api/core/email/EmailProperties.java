package com.gtech.food_api.core.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("foodapi.email")
public class EmailProperties {
    
    @NotNull
    private String sender;
}
