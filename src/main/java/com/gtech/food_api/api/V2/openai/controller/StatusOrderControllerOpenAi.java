package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Status Orders")
public interface StatusOrderControllerOpenAi {
    
    @Operation(summary = "Confirm order", description = "Confirms an order, changing its status from CREATED to CONFIRMED. This indicates the restaurant has accepted the order. Example: Confirm order with code 'abc-123-def' to accept the order.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Order confirmed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order code or order cannot be confirmed", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> confirm(
        @Parameter(description = "Unique order code (UUID format)", required = true, example = "abc-123-def-456") String orderCode
    );

    @Operation(summary = "Deliver order", description = "Marks an order as delivered, changing its status from CONFIRMED to DELIVERED. This indicates the order has been delivered to the customer. Example: Mark order with code 'abc-123-def' as delivered.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Order delivered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order code or order cannot be delivered", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> deliver(
        @Parameter(description = "Unique order code (UUID format)", required = true, example = "abc-123-def-456") String orderCode
    );

    @Operation(summary = "Cancel order", description = "Cancels an order, changing its status to CANCELLED. This can be done by the restaurant or customer. Example: Cancel order with code 'abc-123-def' if the order cannot be fulfilled.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Order cancelled successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid order code or order cannot be cancelled", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<Void> cancel(
        @Parameter(description = "Unique order code (UUID format)", required = true, example = "abc-123-def-456") String orderCode
    );
    
}

