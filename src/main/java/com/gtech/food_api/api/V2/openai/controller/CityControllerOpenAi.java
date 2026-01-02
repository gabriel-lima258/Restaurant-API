package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.CityInput;
import com.gtech.food_api.api.V2.dto.CityDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cities")
public interface CityControllerOpenAi {
    
    @Operation(summary = "List all cities", 
    description = "Retrieves a complete list of all cities registered in the system. Example: São Paulo, Rio de Janeiro, Belo Horizonte.",
    responses = {
        @ApiResponse(responseCode = "200", description = "List of cities retrieved successfully")
    })
    ResponseEntity<CollectionModel<CityDTO>> listAll();

    @Operation(summary = "Find city by ID", description = "Retrieves detailed information about a specific city by its ID. Example: Get city with ID 1 to see its name and associated state.",
    responses = {
        @ApiResponse(responseCode = "200", description = "City found successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid city ID format", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "City not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<CityDTO> findById(
        @Parameter(description = "ID of the city", required = true, example = "1") Long id
    );

    @Operation(summary = "Create a new city", description = "Creates a new city in the system. Requires the city name and the state ID. Example: Create 'Campinas' city associated with state 'São Paulo'.",
    responses = {
        @ApiResponse(responseCode = "201", description = "City created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<CityDTO> save(
        @RequestBody(description = "City data containing the name and state ID", required = true) CityInput cityInput
    );

    @Operation(summary = "Update city", description = "Updates information of an existing city. You can change the city name or associate it with a different state. Example: Update city ID 5 to change its name from 'São Paulo' to 'São Paulo - Capital'.",
    responses = {
        @ApiResponse(responseCode = "200", description = "City updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "City not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<CityDTO> update(
        @Parameter(description = "ID of the city to update", required = true, example = "5") Long id, 
        @RequestBody(description = "City data with updated name and/or state ID", required = true) CityInput cityInput
    );

    @Operation(summary = "Delete city", description = "Removes a city from the system. Note: The city can only be deleted if it's not associated with any restaurant address. Example: Delete city ID 10 if it's no longer needed.",
    responses = {
        @ApiResponse(responseCode = "204", description = "City deleted successfully"),
        @ApiResponse(responseCode = "400", description = "City cannot be deleted (entity in use)", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "City not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "ID of the city", required = true, example = "10") Long id
    );
    
}
