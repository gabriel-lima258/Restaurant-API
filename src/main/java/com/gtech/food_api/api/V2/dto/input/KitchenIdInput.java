package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "KitchenIdInput", description = "Kitchen ID input")
@Getter
@Setter
public class KitchenIdInput {
    @Schema(description = "Kitchen ID", example = "1")
    @NotNull
    private Long id;
}
