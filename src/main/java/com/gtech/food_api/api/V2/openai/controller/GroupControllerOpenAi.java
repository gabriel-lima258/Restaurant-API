package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.GroupInput;
import com.gtech.food_api.api.V2.dto.GroupDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Groups")
public interface GroupControllerOpenAi {
    
    @Operation(summary = "List all groups", description = "Retrieves a complete list of all user groups in the system. Example: Administrators, Managers, Customers.")
    ResponseEntity<CollectionModel<GroupDTO>> listAll();

    @Operation(summary = "Find group by ID", description = "Retrieves detailed information about a specific group by its ID, including associated permissions. Example: Get group ID 1 to see 'Administrators' group details.")
    ResponseEntity<GroupDTO> findById(Long id);

    @Operation(summary = "Create a new group", description = "Creates a new user group in the system. Requires only the group name. Example: Create 'Managers' group.")
    ResponseEntity<GroupDTO> save(GroupInput groupInput);

    @Operation(summary = "Update group", description = "Updates the name of an existing group. Example: Update group ID 2 to change its name from 'Users' to 'Regular Users'.")
    ResponseEntity<GroupDTO> update(Long id, GroupInput groupInput);

    @Operation(summary = "Delete group", description = "Removes a group from the system. Note: The group can only be deleted if no users are associated with it. Example: Delete group ID 5 if it's no longer needed.")
    ResponseEntity<Void> delete(Long id);
    
}

