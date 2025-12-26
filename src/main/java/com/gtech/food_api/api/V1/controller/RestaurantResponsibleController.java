package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.UserDTOAssembler;
import com.gtech.food_api.api.dto.UserDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/restaurants/{restaurantId}/responsibles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantResponsibleController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserDTOAssembler userDTOAssembler;

    @Autowired
    private LinksBuilder linksBuilder;

    @GetMapping
    public ResponseEntity<CollectionModel<UserDTO>> listAll(@PathVariable Long restaurantId){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        CollectionModel<UserDTO> dtoList = userDTOAssembler.toCollectionModel(restaurant.getResponsible())
            .removeLinks()
            .add(linksBuilder.linkToRestaurantResponsible(restaurantId))
            .add(linksBuilder.linkToAddResponsibleRestaurant(restaurantId));

        dtoList.getContent().forEach(userDTO -> {
            userDTO.add(linksBuilder.linkToRemoveResponsibleRestaurant(restaurantId, userDTO.getId()));
        });

        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> addResponsible(@PathVariable Long restaurantId, @PathVariable Long userId){
        restaurantService.addResponsible(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeResponsible(@PathVariable Long restaurantId, @PathVariable Long userId){
        restaurantService.removeResponsible(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

}
