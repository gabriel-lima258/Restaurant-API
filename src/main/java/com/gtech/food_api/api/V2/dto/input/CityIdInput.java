package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "CityIdInput", description = "City ID input")
@Getter
@Setter
public class CityIdInput {
    
    @Schema(description = "City ID", example = "1")
    @NotNull
    private Long id;
}
