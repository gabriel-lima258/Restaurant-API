package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface StatusOrderControllerOpenAi {
    
    ResponseEntity<Void> confirm(String orderCode);

    ResponseEntity<Void> deliver(String orderCode);

    ResponseEntity<Void> cancel(String orderCode);
    
}

