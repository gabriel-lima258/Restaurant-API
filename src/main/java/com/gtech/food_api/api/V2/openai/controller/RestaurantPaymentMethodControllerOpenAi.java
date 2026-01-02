package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurant Payment Methods")
public interface RestaurantPaymentMethodControllerOpenAi {
    
    @Operation(summary = "List restaurant payment methods", description = "Retrieves all payment methods accepted by a specific restaurant. Example: Get all payment methods for restaurant ID 1 to see which payment methods (Credit Card, PIX, Cash) the restaurant accepts.",
    responses = {
        @ApiResponse(responseCode = "200", description = "List of restaurant payment methods retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );

    @Operation(summary = "Associate payment method to restaurant", description = "Associates a payment method to a restaurant, allowing the restaurant to accept that payment method. Example: Associate payment method ID 3 (PIX) to restaurant ID 1 to allow PIX payments.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Payment method associated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid restaurant or payment method ID", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Restaurant or payment method not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> associatePaymentMethod(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @Parameter(description = "ID of the payment method to associate", required = true, example = "3") Long paymentMethodId
    );

    @Operation(summary = "Remove payment method from restaurant", description = "Removes a payment method association from a restaurant, preventing the restaurant from accepting that payment method. Example: Remove payment method ID 2 (Debit Card) from restaurant ID 1 to stop accepting debit card payments.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Payment method removed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid restaurant or payment method ID", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Restaurant, payment method, or association not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> disassociatePaymentMethod(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @Parameter(description = "ID of the payment method to remove", required = true, example = "2") Long paymentMethodId
    );
    
}

