package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.RestaurantControllerV2;
import com.gtech.food_api.api.V2.dto.RestaurantDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter Restaurant em RestaurantDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Restaurant em um DTO RestaurantDTO
 * - toCollectionDTO: converte uma lista de entidades Restaurant em uma lista de DTOs RestaurantDTO
 */
@Component
public class RestaurantDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Restaurant, RestaurantDTO> {
    private final LinksBuilderV2 linksBuilder;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public RestaurantDTOAssemblerV2(LinksBuilderV2 linksBuilder) {
        super(RestaurantControllerV2.class, RestaurantDTO.class);
        this.linksBuilder = linksBuilder;
    }
    @Override
    public RestaurantDTO toModel(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = createModelWithId(restaurant.getId(), restaurant);
        modelMapper.map(restaurant, restaurantDTO);
        restaurantDTO.getKitchen().add(linksBuilder.linkToKitchen(restaurant.getKitchen().getId()));
        restaurantDTO.getAddress().getCity().add(linksBuilder.linkToCity(restaurant.getAddress().getCity().getId()));

        if (usersJwtSecurity.canViewRestaurants()) {
            restaurantDTO.add(linksBuilder.linkToRestaurantPaymentMethods(restaurant.getId(), "paymentMethods"));
        }
        
        if (usersJwtSecurity.canEditRestaurants()) {
            restaurantDTO.add(linksBuilder.linkToRestaurantResponsible(restaurant.getId(), "responsible"));
        }

        if (restaurant.isActive()) {
            restaurantDTO.add(linksBuilder.linkToDeactivateRestaurant(restaurant.getId(), "deactivate"));
        }
        if (restaurant.isOpen()) {
            restaurantDTO.add(linksBuilder.linkToCloseRestaurant(restaurant.getId(), "close"));
        }
        if (restaurant.isClosed()) {
            restaurantDTO.add(linksBuilder.linkToOpenRestaurant(restaurant.getId(), "open"));
        }
        if (restaurant.isNotActive()) {
            restaurantDTO.add(linksBuilder.linkToActivateRestaurant(restaurant.getId(), "activate"));
        }
        restaurantDTO.add(linksBuilder.linkToProducts(restaurant.getId(), "products"));
        return restaurantDTO;
    }

    public RestaurantDTO toModelWithSelf(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = toModel(restaurant);
        restaurantDTO.add(linksBuilder.linkToRestaurants());
        return restaurantDTO;
    }

    @Override
    public CollectionModel<RestaurantDTO> toCollectionModel(Iterable<? extends Restaurant> entities) {
        CollectionModel<RestaurantDTO> collectionModel = super.toCollectionModel(entities);
    
        if (usersJwtSecurity.canViewRestaurants()) {
            collectionModel.add(linksBuilder.linkToRestaurants());
        }
        
        return collectionModel;
    }
}
