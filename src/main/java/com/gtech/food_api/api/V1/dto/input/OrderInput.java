package com.gtech.food_api.api.V1.dto.input;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInput {
    @Valid
    @NotNull
    private RestaurantIdInput restaurant;
    @Valid
    @NotNull
    private PaymentMethodIdInput paymentMethod;
    @Valid
    @NotNull
    private AddressInput deliveryAddress;
    @Valid
    @Size(min = 1)
    @NotNull
    private List<OrderItemInput> items;
}
