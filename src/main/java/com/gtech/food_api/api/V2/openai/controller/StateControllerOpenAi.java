package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.StateInput;
import com.gtech.food_api.api.V2.dto.StateDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")    
@Tag(name = "States")
public interface StateControllerOpenAi {
    
    @Operation(summary = "List all states", description = "Retrieves a complete list of all states (provinces) registered in the system. Example: São Paulo, Rio de Janeiro, Minas Gerais.")
    ResponseEntity<CollectionModel<StateDTO>> listAll();

    @Operation(summary = "Find state by ID", description = "Retrieves detailed information about a specific state by its ID. Example: Get state with ID 1 to see its name and associated cities.")
    ResponseEntity<StateDTO> findById(
        @Parameter(description = "ID of the state", required = true, example = "1") Long id
    );

    @Operation(summary = "Create a new state", description = "Creates a new state in the system. Requires only the state name. Example: Create 'Paraná' state.")
    ResponseEntity<StateDTO> save(
        @RequestBody(description = "State data containing the name", required = true) StateInput stateInput
    );

    @Operation(summary = "Update state", description = "Updates the name of an existing state. Example: Update state ID 3 to change its name from 'Rio' to 'Rio de Janeiro'.")
    ResponseEntity<StateDTO> update(
        @Parameter(description = "ID of the state to update", required = true, example = "3") Long id,
        @RequestBody(description = "State data with updated name", required = true) StateInput stateInput
    );

    @Operation(summary = "Delete state", description = "Removes a state from the system. Note: The state can only be deleted if it has no associated cities. Example: Delete state ID 5 if it's no longer needed.")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID of the state to delete", required = true, example = "5") Long id
    );
    
}

