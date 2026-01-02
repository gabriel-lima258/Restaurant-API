package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "StateInput", description = "State input data")
@Getter
@Setter
public class StateInput {
    @Schema(description = "State name", example = "São Paulo")
    @NotBlank
    private String name;
}
