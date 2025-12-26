package com.gtech.food_api.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInput {
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
}
