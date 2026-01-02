package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.UserInput;
import com.gtech.food_api.api.V2.dto.input.UserPasswordInput;
import com.gtech.food_api.api.V2.dto.input.UserWithPasswordInput;
import com.gtech.food_api.api.V2.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Users")
public interface UserControllerOpenAi {
    
    @Operation(summary = "List all users", description = "Retrieves a complete list of all users registered in the system. Example: Get all users with their names, emails, and associated groups.")
    ResponseEntity<CollectionModel<UserDTO>> listAll();

    @Operation(summary = "Find user by ID", description = "Retrieves detailed information about a specific user by its ID. Example: Get user ID 1 to see their profile, groups, and restaurants.")
    ResponseEntity<UserDTO> findById(Long id);

    @Operation(summary = "Register a new user", description = "Creates a new user account. This endpoint is public and doesn't require authentication. Requires name, email, and password. Example: Register user 'John Doe' with email 'john@example.com' and password.")
    ResponseEntity<UserDTO> save(UserWithPasswordInput userWithPasswordInput);

    @Operation(summary = "Update user profile", description = "Updates user information (name and email). Password is not updated here. Example: Update user ID 5 to change name from 'John' to 'John Smith' or email.")
    ResponseEntity<UserDTO> update(Long id, UserInput userInput);

    @Operation(summary = "Update user password", description = "Changes the user's password. Requires the current password and the new password. Example: Change password for user ID 3 with current password and new secure password.")
    ResponseEntity<Void> updatePassword(Long id, UserPasswordInput userPasswordInput);
    
}

