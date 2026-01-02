package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "KitchenInput", description = "Kitchen input data")
@Getter
@Setter
public class KitchenInput {
    @Schema(description = "Kitchen name", example = "Italian")
    @NotBlank
    private String name;
    
}
