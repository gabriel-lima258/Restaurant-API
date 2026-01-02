package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressInput {
    @Schema(example = "13000-000")
    @NotBlank
    private String cep;
    @Schema(example = "Rua das Flores")
    @NotBlank
    private String publicPlace;
    @Schema(example = "123")
    @NotBlank
    private String number;
    @Schema(example = "Apto 45", required = false)
    private String complement;
    @Schema(example = "Centro")
    @NotBlank
    private String neighborhood;
    @Valid
    @NotNull
    private CityIdInput city;
}
