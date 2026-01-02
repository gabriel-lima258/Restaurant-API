package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.PermissionDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Permissions")
public interface PermissionControllerOpenAi {
    
    @Operation(summary = "List all permissions", description = "Retrieves a complete list of all available permissions in the system. Example: READ_RESTAURANTS, WRITE_ORDERS, MANAGE_USERS, DELETE_PRODUCTS.")
    ResponseEntity<CollectionModel<PermissionDTO>> listAll();
    
}

