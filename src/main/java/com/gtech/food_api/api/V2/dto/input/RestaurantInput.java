package com.gtech.food_api.api.V2.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestaurantInput {
    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    private BigDecimal shippingFee;

    // validacao em cascata, valida a kitchen do restaurant
    @Valid
    @NotNull
    private KitchenIdInput kitchen;

    @Valid
    @NotNull
    private AddressInput address;
}
