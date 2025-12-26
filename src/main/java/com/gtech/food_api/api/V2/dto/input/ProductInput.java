package com.gtech.food_api.api.V2.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductInput {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;   
    @NotNull
    private Boolean active;
}
