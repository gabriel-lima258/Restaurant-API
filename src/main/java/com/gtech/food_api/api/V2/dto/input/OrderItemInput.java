package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "OrderItemInput", description = "Order item input data")
@Getter
@Setter
public class OrderItemInput {
    @Schema(description = "Product ID", example = "1")
    @NotNull
    private Long productId;
    @Schema(description = "Item quantity", example = "2")
    @NotNull
    @Positive
    private Integer quantity;
    @Schema(description = "Item observation", example = "No onions, please")
    private String observation;
}
