package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "UserPasswordInput", description = "User password change input data")
@Getter
@Setter
public class UserPasswordInput {
    @Schema(description = "Current password", example = "OldPassword123!")
    @NotBlank
    private String currentPassword;
    @Schema(description = "New password", example = "NewSecurePassword456!")
    @NotBlank
    private String newPassword;
}
