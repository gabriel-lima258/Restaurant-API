package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.RestaurantInput;
import com.gtech.food_api.api.V2.dto.RestaurantDTO;
import com.gtech.food_api.api.V2.dto.RestaurantSummaryDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

@SecurityRequirement(name = "security_auth")
public interface RestaurantControllerOpenAi {
    
    ResponseEntity<CollectionModel<RestaurantSummaryDTO>> listAll();

    ResponseEntity<RestaurantDTO> findById(Long restaurantId);

    ResponseEntity<RestaurantDTO> save(RestaurantInput restaurantInput);

    ResponseEntity<RestaurantDTO> update(Long restaurantId, RestaurantInput restaurantInput);

    ResponseEntity<Void> delete(Long restaurantId);

    ResponseEntity<Void> activate(Long restaurantId);

    ResponseEntity<Void> deactivate(Long restaurantId);

    ResponseEntity<Void> activateMany(List<Long> restaurantIds);

    ResponseEntity<Void> deactivateMany(List<Long> restaurantIds);

    ResponseEntity<Void> openRestaurant(Long restaurantId);

    ResponseEntity<Void> closeRestaurant(Long restaurantId);

    
}

