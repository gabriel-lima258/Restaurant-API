package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.RestaurantInput;
import com.gtech.food_api.api.V2.dto.RestaurantDTO;
import com.gtech.food_api.api.V2.dto.RestaurantSummaryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurants")
public interface RestaurantControllerOpenAi {
    
    @Operation(summary = "List all restaurants", description = "Retrieves a summary list of all restaurants with basic information. Example: Get all restaurants with name, shipping fee, and kitchen type.")
    ResponseEntity<CollectionModel<RestaurantSummaryDTO>> listAll();

    @Operation(summary = "Find restaurant by ID", description = "Retrieves complete detailed information about a specific restaurant by its ID. Example: Get restaurant ID 1 to see full details including address, payment methods, and products.")
    ResponseEntity<RestaurantDTO> findById(Long restaurantId);

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant in the system. Requires name, shipping fee, kitchen ID, and address. Example: Create 'Pizza Palace' restaurant with R$ 5.00 shipping fee, Italian kitchen, and address.")
    ResponseEntity<RestaurantDTO> save(RestaurantInput restaurantInput);

    @Operation(summary = "Update restaurant", description = "Updates restaurant information including name, shipping fee, kitchen, and address. Example: Update restaurant ID 3 to change shipping fee from R$ 5.00 to R$ 7.00.")
    ResponseEntity<RestaurantDTO> update(Long restaurantId, RestaurantInput restaurantInput);

    @Operation(summary = "Delete restaurant", description = "Removes a restaurant from the system. Note: The restaurant can only be deleted if it has no orders. Example: Delete restaurant ID 10 if it's no longer operating.")
    ResponseEntity<Void> delete(Long restaurantId);

    @Operation(summary = "Activate restaurant", description = "Activates a single restaurant, making it available for orders. Example: Activate restaurant ID 5 to start accepting orders.")
    ResponseEntity<Void> activate(Long restaurantId);

    @Operation(summary = "Deactivate restaurant", description = "Deactivates a single restaurant, preventing new orders. Example: Deactivate restaurant ID 5 to temporarily stop accepting orders.")
    ResponseEntity<Void> deactivate(Long restaurantId);

    @Operation(summary = "Activate multiple restaurants", description = "Activates multiple restaurants at once by providing a list of restaurant IDs. Example: Activate restaurants with IDs [1, 2, 3] in a single request.")
    ResponseEntity<Void> activateMany(List<Long> restaurantIds);

    @Operation(summary = "Deactivate multiple restaurants", description = "Deactivates multiple restaurants at once by providing a list of restaurant IDs. Example: Deactivate restaurants with IDs [1, 2, 3] in a single request.")
    ResponseEntity<Void> deactivateMany(List<Long> restaurantIds);

    @Operation(summary = "Open restaurant", description = "Opens a restaurant, indicating it's currently operating and accepting orders. Example: Open restaurant ID 3 to mark it as currently open.")
    ResponseEntity<Void> openRestaurant(Long restaurantId);

    @Operation(summary = "Close restaurant", description = "Closes a restaurant, indicating it's not currently operating. Example: Close restaurant ID 3 to mark it as currently closed.")
    ResponseEntity<Void> closeRestaurant(Long restaurantId);

    
}

