package com.gtech.food_api.api.dto.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemInput {
    @NotNull
    private Long productId;
    @NotNull
    @Positive
    private Integer quantity;
    private String observation;
}
