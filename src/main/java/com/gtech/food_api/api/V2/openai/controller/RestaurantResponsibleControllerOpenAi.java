package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth") 
@Tag(name = "Restaurant Responsibles")
public interface RestaurantResponsibleControllerOpenAi {
    
    @Operation(summary = "List restaurant responsibles", description = "Retrieves all users who are responsible (managers/owners) for a specific restaurant. Example: Get all responsibles for restaurant ID 1 to see which users can manage this restaurant.")
    ResponseEntity<CollectionModel<UserDTO>> listAll(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );

    @Operation(summary = "Add responsible to restaurant", description = "Adds a user as a responsible (manager/owner) for a restaurant, granting them management permissions. Example: Add user ID 5 as responsible for restaurant ID 1 to allow them to manage the restaurant.")
    ResponseEntity<Void> addResponsible(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @Parameter(description = "ID of the user to add as responsible", required = true, example = "5") Long userId
    );

    @Operation(summary = "Remove responsible from restaurant", description = "Removes a user as a responsible from a restaurant, revoking their management permissions. Example: Remove user ID 5 as responsible from restaurant ID 1 to revoke management access.")
    ResponseEntity<Void> removeResponsible(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @Parameter(description = "ID of the user to remove as responsible", required = true, example = "5") Long userId
    );
    
}

