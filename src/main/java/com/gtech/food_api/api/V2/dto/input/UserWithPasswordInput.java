package com.gtech.food_api.api.V1.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithPasswordInput extends UserInput {
    @NotBlank
    private String password;
}
