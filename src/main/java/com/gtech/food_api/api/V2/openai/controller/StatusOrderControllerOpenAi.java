package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Status Orders")
public interface StatusOrderControllerOpenAi {
    
    @Operation(summary = "Confirm order", description = "Confirms an order, changing its status from CREATED to CONFIRMED. This indicates the restaurant has accepted the order. Example: Confirm order with code 'abc-123-def' to accept the order.")
    ResponseEntity<Void> confirm(String orderCode);

    @Operation(summary = "Deliver order", description = "Marks an order as delivered, changing its status from CONFIRMED to DELIVERED. This indicates the order has been delivered to the customer. Example: Mark order with code 'abc-123-def' as delivered.")
    ResponseEntity<Void> deliver(String orderCode);

    @Operation(summary = "Cancel order", description = "Cancels an order, changing its status to CANCELLED. This can be done by the restaurant or customer. Example: Cancel order with code 'abc-123-def' if the order cannot be fulfilled.")
    ResponseEntity<Void> cancel(String orderCode);
    
}

