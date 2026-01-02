package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.RestaurantInput;
import com.gtech.food_api.api.V2.dto.RestaurantDTO;
import com.gtech.food_api.api.V2.dto.RestaurantSummaryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurants")
public interface RestaurantControllerOpenAi {
    
    @Operation(summary = "List all restaurants", description = "Retrieves a summary list of all restaurants with basic information. Example: Get all restaurants with name, shipping fee, and kitchen type.")
    ResponseEntity<CollectionModel<RestaurantSummaryDTO>> listAll();

    @Operation(summary = "Find restaurant by ID", description = "Retrieves complete detailed information about a specific restaurant by its ID. Example: Get restaurant ID 1 to see full details including address, payment methods, and products.")
    ResponseEntity<RestaurantDTO> findById(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant in the system. Requires name, shipping fee, kitchen ID, and address. Example: Create 'Pizza Palace' restaurant with R$ 5.00 shipping fee, Italian kitchen, and address.")
    ResponseEntity<RestaurantDTO> save(
        @RequestBody(description = "Restaurant data containing name, shipping fee, kitchen ID, and address", required = true) RestaurantInput restaurantInput
    );

    @Operation(summary = "Update restaurant", description = "Updates restaurant information including name, shipping fee, kitchen, and address. Example: Update restaurant ID 3 to change shipping fee from R$ 5.00 to R$ 7.00.")
    ResponseEntity<RestaurantDTO> update(
        @Parameter(description = "ID of the restaurant to update", required = true, example = "3") Long restaurantId,
        @RequestBody(description = "Restaurant data with updated information", required = true) RestaurantInput restaurantInput
    );

    @Operation(summary = "Delete restaurant", description = "Removes a restaurant from the system. Note: The restaurant can only be deleted if it has no orders. Example: Delete restaurant ID 10 if it's no longer operating.")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID of the restaurant to delete", required = true, example = "10") Long restaurantId
    );

    @Operation(summary = "Activate restaurant", description = "Activates a single restaurant, making it available for orders. Example: Activate restaurant ID 5 to start accepting orders.")
    ResponseEntity<Void> activate(
        @Parameter(description = "ID of the restaurant to activate", required = true, example = "5") Long restaurantId
    );

    @Operation(summary = "Deactivate restaurant", description = "Deactivates a single restaurant, preventing new orders. Example: Deactivate restaurant ID 5 to temporarily stop accepting orders.")
    ResponseEntity<Void> deactivate(
        @Parameter(description = "ID of the restaurant to deactivate", required = true, example = "5") Long restaurantId
    );

    @Operation(summary = "Activate multiple restaurants", description = "Activates multiple restaurants at once by providing a list of restaurant IDs. Example: Activate restaurants with IDs [1, 2, 3] in a single request.")
    ResponseEntity<Void> activateMany(
        @RequestBody(description = "List of restaurant IDs to activate. Example: [1, 2, 3]", required = true) List<Long> restaurantIds
    );

    @Operation(summary = "Deactivate multiple restaurants", description = "Deactivates multiple restaurants at once by providing a list of restaurant IDs. Example: Deactivate restaurants with IDs [1, 2, 3] in a single request.")
    ResponseEntity<Void> deactivateMany(
        @RequestBody(description = "List of restaurant IDs to deactivate. Example: [1, 2, 3]", required = true) List<Long> restaurantIds
    );

    @Operation(summary = "Open restaurant", description = "Opens a restaurant, indicating it's currently operating and accepting orders. Example: Open restaurant ID 3 to mark it as currently open.")
    ResponseEntity<Void> openRestaurant(
        @Parameter(description = "ID of the restaurant to open", required = true, example = "3") Long restaurantId
    );

    @Operation(summary = "Close restaurant", description = "Closes a restaurant, indicating it's not currently operating. Example: Close restaurant ID 3 to mark it as currently closed.")
    ResponseEntity<Void> closeRestaurant(
        @Parameter(description = "ID of the restaurant to close", required = true, example = "3") Long restaurantId
    );

    
}

