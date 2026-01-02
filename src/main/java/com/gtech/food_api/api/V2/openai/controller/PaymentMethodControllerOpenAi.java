package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.gtech.food_api.api.V2.dto.input.PaymentMethodInput;
import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Payment Methods")
public interface PaymentMethodControllerOpenAi {
    
    @Operation(summary = "List all payment methods", description = "Retrieves a complete list of all payment methods with cache support (10 seconds). Example: Credit Card, Debit Card, Cash, PIX.")
    ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(ServletWebRequest request);

    @Operation(summary = "Find payment method by ID", description = "Retrieves detailed information about a specific payment method by its ID. Example: Get payment method ID 1 to see 'Credit Card' details.")
    ResponseEntity<PaymentMethodDTO> findById(
        @Parameter(description = "ID of the payment method", required = true, example = "1") Long id
    );

    @Operation(summary = "Create a new payment method", description = "Creates a new payment method in the system. Requires the payment method description. Example: Create 'PIX' payment method.")
    ResponseEntity<PaymentMethodDTO> save(
        @RequestBody(description = "Payment method data containing the description", required = true) PaymentMethodInput paymentMethodInput
    );

    @Operation(summary = "Update payment method", description = "Updates the description of an existing payment method. Example: Update payment method ID 2 to change its description from 'Credit' to 'Credit Card'.")
    ResponseEntity<PaymentMethodDTO> update(
        @Parameter(description = "ID of the payment method to update", required = true, example = "2") Long id,
        @RequestBody(description = "Payment method data with updated description", required = true) PaymentMethodInput paymentMethodInput
    );

    @Operation(summary = "Delete payment method", description = "Removes a payment method from the system. Note: The payment method can only be deleted if no restaurants are using it. Example: Delete payment method ID 5 if it's no longer needed.")
    ResponseEntity<Void> delete(
        @Parameter(description = "ID of the payment method to delete", required = true, example = "5") Long id
    );
    
}

