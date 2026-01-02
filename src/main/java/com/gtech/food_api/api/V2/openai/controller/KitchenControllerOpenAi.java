package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.KitchenInput;
import com.gtech.food_api.core.springdoc.PageableParameters;
import com.gtech.food_api.api.V2.dto.KitchenDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Kitchens")
public interface KitchenControllerOpenAi {
    
    @PageableParameters
    @Operation(summary = "List all kitchens", description = "Retrieves a paginated list of all kitchen types. Supports pagination with page and size parameters. Example: Get first page with 10 kitchens (Italian, Brazilian, Japanese, etc.).",
    responses = {
        @ApiResponse(responseCode = "200", description = "Paginated list of kitchens retrieved successfully")
    })
    ResponseEntity<PagedModel<KitchenDTO>> listAll(
        @Parameter(description = "Pagination parameters (page, size, sort). Default: page=0, size=10", required = false, example = "page=0&size=10") Pageable pageable
    );

    @Operation(summary = "Find kitchen by ID", description = "Retrieves detailed information about a specific kitchen type by its ID. Example: Get kitchen ID 1 to see 'Italian' kitchen details.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Kitchen found successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid kitchen ID format", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Kitchen not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<KitchenDTO> findById(
        @Parameter(description = "ID of the kitchen", required = true, example = "1") Long id
    );

    @Operation(summary = "Create a new kitchen", description = "Creates a new kitchen type in the system. Requires only the kitchen name. Example: Create 'Mexican' kitchen type.",
    responses = {
        @ApiResponse(responseCode = "201", description = "Kitchen created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<KitchenDTO> save(
        @RequestBody(description = "Kitchen data containing the name", required = true) KitchenInput kitchenInput
    );

    @Operation(summary = "Update kitchen", description = "Updates the name of an existing kitchen type. Example: Update kitchen ID 2 to change its name from 'Fast Food' to 'American Fast Food'.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Kitchen updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Kitchen not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<KitchenDTO> update(
        @Parameter(description = "ID of the kitchen to update", required = true, example = "2") Long id,
        @RequestBody(description = "Kitchen data with updated name", required = true) KitchenInput kitchenInput
    );

    @Operation(summary = "Delete kitchen", description = "Removes a kitchen type from the system. Note: The kitchen can only be deleted if no restaurants are using it. Example: Delete kitchen ID 5 if it's no longer needed.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Kitchen deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Kitchen cannot be deleted (entity in use)", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Kitchen not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> delete(
        @Parameter(description = "ID of the kitchen to delete", required = true, example = "5") Long id
    );
    
}

