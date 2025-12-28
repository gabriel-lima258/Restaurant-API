package com.gtech.food_api.core.security.authorization;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties("foodapi.auth")
public class ApiSecurityProperties {

    // providencia a url do authorization server
    @NotBlank
    private String providerUrl;
    
}
