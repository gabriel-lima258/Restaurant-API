package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.UserInput;
import com.gtech.food_api.api.V2.dto.input.UserPasswordInput;
import com.gtech.food_api.api.V2.dto.input.UserWithPasswordInput;
import com.gtech.food_api.api.V2.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Users")
public interface UserControllerOpenAi {
    
    @Operation(summary = "List all users", description = "Retrieves a complete list of all users registered in the system. Example: Get all users with their names, emails, and associated groups.",
    responses = {
        @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    ResponseEntity<CollectionModel<UserDTO>> listAll();

    @Operation(summary = "Find user by ID", description = "Retrieves detailed information about a specific user by its ID. Example: Get user ID 1 to see their profile, groups, and restaurants.",
    responses = {
        @ApiResponse(responseCode = "200", description = "User found successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID format", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<UserDTO> findById(
        @Parameter(description = "ID of the user", required = true, example = "1") Long id
    );

    @Operation(summary = "Register a new user", description = "Creates a new user account. This endpoint is public and doesn't require authentication. Requires name, email, and password. Example: Register user 'John Doe' with email 'john@example.com' and password.",
    responses = {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<UserDTO> save(
        @RequestBody(description = "User registration data containing name, email, and password", required = true) UserWithPasswordInput userWithPasswordInput
    );

    @Operation(summary = "Update user profile", description = "Updates user information (name and email). Password is not updated here. Example: Update user ID 5 to change name from 'John' to 'John Smith' or email.",
    responses = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data or validation errors", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<UserDTO> update(
        @Parameter(description = "ID of the user to update", required = true, example = "5") Long id,
        @RequestBody(description = "User data with updated name and/or email", required = true) UserInput userInput
    );

    @Operation(summary = "Update user password", description = "Changes the user's password. Requires the current password and the new password. Example: Change password for user ID 3 with current password and new secure password.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password or current password mismatch", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> updatePassword(
        @Parameter(description = "ID of the user whose password will be changed", required = true, example = "3") Long id,
        @RequestBody(description = "Password data containing current password and new password", required = true) UserPasswordInput userPasswordInput
    );
    
}

