package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.KitchenInput;
import com.gtech.food_api.api.V2.dto.KitchenDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Kitchens")
public interface KitchenControllerOpenAi {
    
    @Operation(summary = "List all kitchens", description = "Retrieves a paginated list of all kitchen types. Supports pagination with page and size parameters. Example: Get first page with 10 kitchens (Italian, Brazilian, Japanese, etc.).")
    ResponseEntity<PagedModel<KitchenDTO>> listAll(Pageable pageable);

    @Operation(summary = "Find kitchen by ID", description = "Retrieves detailed information about a specific kitchen type by its ID. Example: Get kitchen ID 1 to see 'Italian' kitchen details.")
    ResponseEntity<KitchenDTO> findById(Long id);

    @Operation(summary = "Create a new kitchen", description = "Creates a new kitchen type in the system. Requires only the kitchen name. Example: Create 'Mexican' kitchen type.")
    ResponseEntity<KitchenDTO> save(KitchenInput kitchenInput);

    @Operation(summary = "Update kitchen", description = "Updates the name of an existing kitchen type. Example: Update kitchen ID 2 to change its name from 'Fast Food' to 'American Fast Food'.")
    ResponseEntity<KitchenDTO> update(Long id, KitchenInput kitchenInput);

    @Operation(summary = "Delete kitchen", description = "Removes a kitchen type from the system. Note: The kitchen can only be deleted if no restaurants are using it. Example: Delete kitchen ID 5 if it's no longer needed.")
    ResponseEntity<Void> delete(Long id);
    
}

