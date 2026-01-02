package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "UserWithPasswordInput", description = "User registration input data with password")
@Getter
@Setter
public class UserWithPasswordInput extends UserInput {
    @Schema(description = "User password", example = "SecurePassword123!")
    @NotBlank
    private String password;
}
