package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "CityInput", description = "City input data")
@Getter
@Setter
public class CityInput {
    
    @Schema(description = "City name", example = "Campinas")
    @NotBlank
    private String name;

    @Schema(description = "State information")
    @Valid
    @NotNull
    private StateIdInput state;
}
