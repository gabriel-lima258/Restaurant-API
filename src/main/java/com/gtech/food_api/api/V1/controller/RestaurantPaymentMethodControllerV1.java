package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.PaymentMethodDTOAssemblerV1;
import com.gtech.food_api.api.V1.dto.PaymentMethodDTO;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciar os métodos de pagamento de um restaurante
 * 
 * Este endpoint é responsável por gerenciar os métodos de pagamento de um restaurante
 * Ele mapeia Many to Many entre Restaurant e PaymentMethod
 */
@RestController
@RequestMapping(value = "/v1/restaurants/{restaurantId}/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantPaymentMethodControllerV1 {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PaymentMethodDTOAssemblerV1 paymentMethodDTOAssembler;


    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> listAll(@PathVariable Long restaurantId){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        List<PaymentMethodDTO> dtoList = paymentMethodDTOAssembler.toCollectionDTO(restaurant.getPaymentMethods());

        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/{paymentMethodId}")
    public ResponseEntity<Void> associatePaymentMethod(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
        restaurantService.associatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/{paymentMethodId}")
    public ResponseEntity<Void> disassociatePaymentMethod(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
        restaurantService.disassociatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }

}
