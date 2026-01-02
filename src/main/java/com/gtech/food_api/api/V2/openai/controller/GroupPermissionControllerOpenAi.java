package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.PermissionDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Group Permissions")
public interface GroupPermissionControllerOpenAi {
    
    @Operation(summary = "List group permissions", description = "Retrieves all permissions associated with a specific group. Example: Get all permissions for group ID 1 (Administrators) to see which permissions (READ_RESTAURANTS, WRITE_ORDERS, etc.) the group has.",
    responses = {
        @ApiResponse(responseCode = "200", description = "List of group permissions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Group not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<CollectionModel<PermissionDTO>> listAll(
        @Parameter(description = "ID of the group", required = true, example = "1") Long groupId
    );

    @Operation(summary = "Add permission to group", description = "Adds a permission to a group, granting all users in that group the new permission. Example: Add permission ID 5 (WRITE_ORDERS) to group ID 2 (Managers) to allow managers to create orders.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Permission added successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid group or permission ID", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Group or permission not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> addPermission(
        @Parameter(description = "ID of the group", required = true, example = "2") Long groupId,
        @Parameter(description = "ID of the permission to add", required = true, example = "5") Long permissionId
    );

    @Operation(summary = "Remove permission from group", description = "Removes a permission from a group, revoking that permission from all users in the group. Example: Remove permission ID 5 (WRITE_ORDERS) from group ID 2 (Managers) to revoke order creation permission.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Permission removed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid group or permission ID", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Group, permission, or association not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> removePermission(
        @Parameter(description = "ID of the group", required = true, example = "2") Long groupId,
        @Parameter(description = "ID of the permission to remove", required = true, example = "5") Long permissionId
    );
    
}

