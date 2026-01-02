package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "AddressInput", description = "Address input data")
@Getter
@Setter
public class AddressInput {
    @Schema(description = "ZIP code", example = "13000-000")
    @NotBlank
    private String cep;
    @Schema(description = "Public place (street, avenue, etc.)", example = "Rua das Flores")
    @NotBlank
    private String publicPlace;
    @Schema(description = "Address number", example = "123")
    @NotBlank
    private String number;
    @Schema(description = "Address complement", example = "Apto 45")
    private String complement;
    @Schema(description = "Neighborhood", example = "Centro")
    @NotBlank
    private String neighborhood;
    @Schema(description = "City information")
    @Valid
    @NotNull
    private CityIdInput city;
}
