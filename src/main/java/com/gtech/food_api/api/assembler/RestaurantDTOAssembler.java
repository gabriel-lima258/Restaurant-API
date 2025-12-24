package com.gtech.food_api.api.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.controller.RestaurantController;
import com.gtech.food_api.api.dto.RestaurantDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter Restaurant em RestaurantDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Restaurant em um DTO RestaurantDTO
 * - toCollectionDTO: converte uma lista de entidades Restaurant em uma lista de DTOs RestaurantDTO
 */
@Component
public class RestaurantDTOAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantDTO> {

    private final LinksBuilder linksBuilder;

    @Autowired
    private ModelMapper modelMapper;

    public RestaurantDTOAssembler(LinksBuilder linksBuilder) {
        super(RestaurantController.class, RestaurantDTO.class);
        this.linksBuilder = linksBuilder;
    }

    @Override
    public RestaurantDTO toModel(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = createModelWithId(restaurant.getId(), restaurant);
        modelMapper.map(restaurant, restaurantDTO);

        restaurantDTO.getKitchen().add(linksBuilder.linkToKitchen(restaurant.getKitchen().getId()));
        restaurantDTO.getAddress().getCity().add(linksBuilder.linkToCity(restaurant.getAddress().getCity().getId()));


        restaurantDTO.add(linksBuilder.linkToRestaurantPaymentMethods(restaurant.getId(), "paymentMethods"));
        restaurantDTO.add(linksBuilder.linkToRestaurantResponsible(restaurant.getId(), "responsible"));

        return restaurantDTO;
    }

    public RestaurantDTO toModelWithSelf(Restaurant restaurant) {
        RestaurantDTO restaurantDTO = toModel(restaurant);

        restaurantDTO.add(linksBuilder.linkToRestaurants());
 
        return restaurantDTO;
    }

    @Override
    public CollectionModel<RestaurantDTO> toCollectionModel(Iterable<? extends Restaurant> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToRestaurants());
    }

    // public RestaurantDTO copyToDTO(Restaurant restaurant) {
    //     // Restaurant -> RestaurantDTO
    //     return modelMapper.map(restaurant, RestaurantDTO.class);
    // }
    
    // public List<RestaurantDTO> toCollectionDTO(Collection<Restaurant> restaurants) {
    //     // List<Restaurant> -> List<RestaurantDTO>
    //     return restaurants.stream()
    //         .map(entity -> copyToDTO(entity))
    //         .collect(Collectors.toList());
    // }
}
