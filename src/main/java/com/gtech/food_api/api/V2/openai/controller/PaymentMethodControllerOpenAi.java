package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import com.gtech.food_api.api.V2.dto.input.PaymentMethodInput;
import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface PaymentMethodControllerOpenAi {
    
    ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(ServletWebRequest request);

    ResponseEntity<PaymentMethodDTO> findById(Long id);

    ResponseEntity<PaymentMethodDTO> save(PaymentMethodInput paymentMethodInput);

    ResponseEntity<PaymentMethodDTO> update(Long id, PaymentMethodInput paymentMethodInput);

    ResponseEntity<Void> delete(Long id);
    
}

