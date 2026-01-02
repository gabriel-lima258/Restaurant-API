package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "UserInput", description = "User input data")
@Getter
@Setter
public class UserInput {
    @Schema(description = "User name", example = "John Doe")
    @NotBlank
    private String name;
    @Schema(description = "User email", example = "john.doe@example.com")
    @NotBlank
    @Email
    private String email;
}
