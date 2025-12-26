package com.gtech.food_api.api.V2.dto.input;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
