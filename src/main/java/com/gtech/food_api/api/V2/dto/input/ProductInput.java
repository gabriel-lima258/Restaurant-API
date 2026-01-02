package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(name = "ProductInput", description = "Product input data")
@Getter
@Setter
public class ProductInput {
    @Schema(description = "Product name", example = "Pizza Margherita")
    @NotBlank
    private String name;
    @Schema(description = "Product description", example = "Delicious pizza with tomato, mozzarella and basil")
    @NotBlank
    private String description;
    @Schema(description = "Product price", example = "35.00")
    @NotNull
    @PositiveOrZero
    private BigDecimal price;   
    @Schema(description = "Product active status", example = "true")
    @NotNull
    private Boolean active;
}
