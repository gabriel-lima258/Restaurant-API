package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "StateIdInput", description = "State ID input")
@Getter
@Setter 
public class StateIdInput {
    @Schema(description = "State ID", example = "1")
    @NotNull
    private Long id;
    
}
