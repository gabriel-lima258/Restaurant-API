package com.gtech.food_api.api.V2.dto.input;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(name = "OrderInput", description = "Order input data")
@Getter
@Setter
public class OrderInput {
    @Schema(description = "Restaurant information")
    @Valid
    @NotNull
    private RestaurantIdInput restaurant;
    @Schema(description = "Payment method information")
    @Valid
    @NotNull
    private PaymentMethodIdInput paymentMethod;
    @Schema(description = "Delivery address")
    @Valid
    @NotNull
    private AddressInput deliveryAddress;
    @Schema(description = "Order items (minimum 1 item required)")
    @Valid
    @Size(min = 1)
    @NotNull
    private List<OrderItemInput> items;
}
