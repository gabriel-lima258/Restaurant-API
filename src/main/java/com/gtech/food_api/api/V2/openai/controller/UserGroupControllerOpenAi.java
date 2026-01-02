package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.GroupDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "User Groups")
public interface UserGroupControllerOpenAi {
    
    @Operation(summary = "List user groups", description = "Retrieves all groups associated with a specific user. Example: Get all groups for user ID 1 to see which groups (Administrators, Managers, etc.) the user belongs to.")
    ResponseEntity<CollectionModel<GroupDTO>> listAll(Long userId);

    @Operation(summary = "Associate group to user", description = "Associates a group to a user, granting the user all permissions from that group. Example: Associate group ID 2 (Managers) to user ID 1 to grant manager permissions.")
    ResponseEntity<Void> associateGroup(Long userId, Long groupId);

    @Operation(summary = "Remove group from user", description = "Removes a group association from a user, revoking all permissions from that group. Example: Remove group ID 2 (Managers) from user ID 1 to revoke manager permissions.")
    ResponseEntity<Void> disassociateGroup(Long userId, Long groupId);
    
}

