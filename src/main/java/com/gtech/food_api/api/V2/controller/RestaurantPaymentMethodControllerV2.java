package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.PaymentMethodDTOAssemblerV2;
import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;
import com.gtech.food_api.api.V2.openai.controller.RestaurantPaymentMethodControllerOpenAi;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.core.security.resource.validations.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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
@RequestMapping(value = "/v2/restaurants/{restaurantId}/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantPaymentMethodControllerV2 implements RestaurantPaymentMethodControllerOpenAi {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PaymentMethodDTOAssemblerV2 paymentMethodDTOAssembler;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    @CheckSecurity.Restaurants.CanView
    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(@PathVariable Long restaurantId){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        CollectionModel<PaymentMethodDTO> dtoList = paymentMethodDTOAssembler.toCollectionModel(restaurant.getPaymentMethods())
        .removeLinks()
        .add(linksBuilder.linkToRestaurantPaymentMethods(restaurantId, "payment-methods"));

        if (usersJwtSecurity.canManagerOwnerRestaurant(restaurantId)) {
            dtoList.add(linksBuilder.linkToAssociatePaymentMethodRestaurant(restaurantId));
            dtoList.getContent().forEach(paymentMethodDTO -> {
                paymentMethodDTO.add(linksBuilder.linkToDesassociatePaymentMethodRestaurant(restaurantId, paymentMethodDTO.getId()));
            });
        }

        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.Restaurants.CanOnwerManager
    @Override
    @PutMapping("/{paymentMethodId}")
    public ResponseEntity<Void> associatePaymentMethod(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
        restaurantService.associatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }
    
    @CheckSecurity.Restaurants.CanOnwerManager
    @Override
    @DeleteMapping("/{paymentMethodId}")
    public ResponseEntity<Void> disassociatePaymentMethod(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
        restaurantService.disassociatePaymentMethod(restaurantId, paymentMethodId);
        return ResponseEntity.noContent().build();
    }

}
