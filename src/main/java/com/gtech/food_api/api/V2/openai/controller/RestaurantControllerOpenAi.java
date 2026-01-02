package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.RestaurantInput;
import com.gtech.food_api.api.V2.dto.RestaurantDTO;
import com.gtech.food_api.api.V2.dto.RestaurantSummaryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurants")
public interface RestaurantControllerOpenAi {
    
    @Operation(summary = "List all restaurants", description = "Retrieves a summary list of all restaurants with basic information. Example: Get all restaurants with name, shipping fee, and kitchen type.",
    responses = {
        @ApiResponse(responseCode = "200", description = "List of restaurants retrieved successfully")
    })
    ResponseEntity<CollectionModel<RestaurantSummaryDTO>> listAll();

    @Operation(summary = "Find restaurant by ID", description = "Retrieves complete detailed information about a specific restaurant by its ID. Example: Get restaurant ID 1 to see full details including address, payment methods, and products.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Restaurant found successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid restaurant ID format", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<RestaurantDTO> findById(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );

    @Operation(summary = "Create a new restaurant", description = "Creates a new restaurant in the system. Requires name, shipping fee, kitchen ID, and address. Example: Create 'Pizza Palace' restaurant with R$ 5.00 shipping fee, Italian kitchen, and address.",
    responses = {
        @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<RestaurantDTO> save(
        @RequestBody(description = "Restaurant data containing name, shipping fee, kitchen ID, and address", required = true) RestaurantInput restaurantInput
    );

    @Operation(summary = "Update restaurant", description = "Updates restaurant information including name, shipping fee, kitchen, and address. Example: Update restaurant ID 3 to change shipping fee from R$ 5.00 to R$ 7.00.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<RestaurantDTO> update(
        @Parameter(description = "ID of the restaurant to update", required = true, example = "3") Long restaurantId,
        @RequestBody(description = "Restaurant data with updated information", required = true) RestaurantInput restaurantInput
    );

    @Operation(summary = "Delete restaurant", description = "Removes a restaurant from the system. Note: The restaurant can only be deleted if it has no orders. Example: Delete restaurant ID 10 if it's no longer operating.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Restaurant cannot be deleted (entity in use)", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "ID of the restaurant to delete", required = true, example = "10") Long restaurantId
    );

    @Operation(summary = "Activate restaurant", description = "Activates a single restaurant, making it available for orders. Example: Activate restaurant ID 5 to start accepting orders.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurant activated successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> activate(
        @Parameter(description = "ID of the restaurant to activate", required = true, example = "5") Long restaurantId
    );

    @Operation(summary = "Deactivate restaurant", description = "Deactivates a single restaurant, preventing new orders. Example: Deactivate restaurant ID 5 to temporarily stop accepting orders.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurant deactivated successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> deactivate(
        @Parameter(description = "ID of the restaurant to deactivate", required = true, example = "5") Long restaurantId
    );

    @Operation(summary = "Activate multiple restaurants", description = "Activates multiple restaurants at once by providing a list of restaurant IDs. Example: Activate restaurants with IDs [1, 2, 3] in a single request.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurants activated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> activateMany(
        @RequestBody(description = "List of restaurant IDs to activate. Example: [1, 2, 3]", required = true) List<Long> restaurantIds
    );

    @Operation(summary = "Deactivate multiple restaurants", description = "Deactivates multiple restaurants at once by providing a list of restaurant IDs. Example: Deactivate restaurants with IDs [1, 2, 3] in a single request.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurants deactivated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> deactivateMany(
        @RequestBody(description = "List of restaurant IDs to deactivate. Example: [1, 2, 3]", required = true) List<Long> restaurantIds
    );

    @Operation(summary = "Open restaurant", description = "Opens a restaurant, indicating it's currently operating and accepting orders. Example: Open restaurant ID 3 to mark it as currently open.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurant opened successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> openRestaurant(
        @Parameter(description = "ID of the restaurant to open", required = true, example = "3") Long restaurantId
    );

    @Operation(summary = "Close restaurant", description = "Closes a restaurant, indicating it's not currently operating. Example: Close restaurant ID 3 to mark it as currently closed.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Restaurant closed successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> closeRestaurant(
        @Parameter(description = "ID of the restaurant to close", required = true, example = "3") Long restaurantId
    );

    
}

