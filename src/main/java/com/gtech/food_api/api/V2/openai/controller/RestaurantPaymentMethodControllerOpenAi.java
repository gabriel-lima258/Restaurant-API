package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface RestaurantPaymentMethodControllerOpenAi {
    
    ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(Long restaurantId);

    ResponseEntity<Void> associatePaymentMethod(Long restaurantId, Long paymentMethodId);

    ResponseEntity<Void> disassociatePaymentMethod(Long restaurantId, Long paymentMethodId);
    
}

