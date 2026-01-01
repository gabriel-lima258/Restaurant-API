package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.UserDTOAssemblerV2;
import com.gtech.food_api.api.V2.dto.UserDTO;
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
@RequestMapping(value = "/v2/restaurants/{restaurantId}/responsibles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantResponsibleControllerV2 {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserDTOAssemblerV2 userDTOAssembler;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    @CheckSecurity.Restaurants.CanView
    @GetMapping
    public ResponseEntity<CollectionModel<UserDTO>> listAll(@PathVariable Long restaurantId){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        CollectionModel<UserDTO> dtoList = userDTOAssembler.toCollectionModel(restaurant.getResponsible())
            .removeLinks()
            .add(linksBuilder.linkToRestaurantResponsible(restaurantId));

        if (usersJwtSecurity.canManagerOwnerRestaurant(restaurantId)) {
            dtoList.add(linksBuilder.linkToAddResponsibleRestaurant(restaurantId));
            dtoList.getContent().forEach(userDTO -> {
                userDTO.add(linksBuilder.linkToRemoveResponsibleRestaurant(restaurantId, userDTO.getId()));
            });
        }

        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.Restaurants.CanAdminManager
    @PutMapping("/{userId}")
    public ResponseEntity<Void> addResponsible(@PathVariable Long restaurantId, @PathVariable Long userId){
        restaurantService.addResponsible(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurants.CanAdminManager
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeResponsible(@PathVariable Long restaurantId, @PathVariable Long userId){
        restaurantService.removeResponsible(restaurantId, userId);
        return ResponseEntity.noContent().build();
    }

}
