package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Schema(name = "RestaurantInput", description = "Restaurant input data")
@Getter
@Setter
public class RestaurantInput {
    @Schema(description = "Restaurant name", example = "Pizza Palace")
    @NotBlank
    private String name;

    @Schema(description = "Shipping fee", example = "5.50")
    @NotNull
    @PositiveOrZero
    private BigDecimal shippingFee;

    @Schema(description = "Kitchen information")
    // validacao em cascata, valida a kitchen do restaurant
    @Valid
    @NotNull
    private KitchenIdInput kitchen;

    @Schema(description = "Restaurant address")
    @Valid
    @NotNull
    private AddressInput address;
}
