package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "GroupInput", description = "Group input data")
@Getter
@Setter
public class GroupInput {
    @Schema(description = "Group name", example = "Managers")
    @NotBlank
    private String name;
    
}
