package com.gtech.food_api.api.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressInput {
    @NotBlank
    private String cep;
    @NotBlank
    private String publicPlace;
    @NotBlank
    private String number;
    private String complement;
    @NotBlank
    private String neighborhood;
    @Valid
    @NotNull
    private CityIdInput city;
}
