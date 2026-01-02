package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInput {
    @Schema(example = "1")
    @NotNull
    private Long productId;
    @Schema(example = "2")
    @NotNull
    @Positive
    private Integer quantity;
    @Schema(example = "No onions, please", required = false)
    private String observation;
}
