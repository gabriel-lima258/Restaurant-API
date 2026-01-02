package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.UserDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface RestaurantResponsibleControllerOpenAi {
    
    ResponseEntity<CollectionModel<UserDTO>> listAll(Long restaurantId);

    ResponseEntity<Void> addResponsible(Long restaurantId, Long userId);

    ResponseEntity<Void> removeResponsible(Long restaurantId, Long userId);
    
}

