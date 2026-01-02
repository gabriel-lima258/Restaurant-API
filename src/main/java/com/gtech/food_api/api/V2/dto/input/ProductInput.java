package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductInput {
    @Schema(example = "Pizza Margherita")
    @NotBlank
    private String name;
    @Schema(example = "Delicious pizza with tomato, mozzarella and basil")
    @NotBlank
    private String description;
    @Schema(example = "35.00")
    @NotNull
    @PositiveOrZero
    private BigDecimal price;   
    @Schema(example = "true")
    @NotNull
    private Boolean active;
}
