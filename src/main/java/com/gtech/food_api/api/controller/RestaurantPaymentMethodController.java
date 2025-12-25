package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.PaymentMethodDTOAssembler;
import com.gtech.food_api.api.dto.PaymentMethodDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Controller para gerenciar os métodos de pagamento de um restaurante
 * 
 * Este endpoint é responsável por gerenciar os métodos de pagamento de um restaurante
 * Ele mapeia Many to Many entre Restaurant e PaymentMethod
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/payment-methods")
public class RestaurantPaymentMethodController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PaymentMethodDTOAssembler paymentMethodDTOAssembler;

    @Autowired
    private LinksBuilder linksBuilder;

    @GetMapping
    public ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(@PathVariable Long restaurantId){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        CollectionModel<PaymentMethodDTO> dtoList = paymentMethodDTOAssembler.toCollectionModel(restaurant.getPaymentMethods())
        .removeLinks()
        .add(linksBuilder.linkToRestaurantPaymentMethods(restaurantId, "payment-methods"))
        .add(linksBuilder.linkToAssociatePaymentMethodRestaurant(restaurantId));

        dtoList.getContent().forEach(paymentMethodDTO -> {
            paymentMethodDTO.add(linksBuilder.linkToDesassociatePaymentMethodRestaurant(restaurantId, paymentMethodDTO.getId()));
        });

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
