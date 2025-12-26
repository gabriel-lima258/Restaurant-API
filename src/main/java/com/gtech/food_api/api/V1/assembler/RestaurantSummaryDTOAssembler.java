package com.gtech.food_api.api.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.controller.RestaurantController;
import com.gtech.food_api.api.dto.RestaurantSummaryDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class RestaurantSummaryDTOAssembler extends RepresentationModelAssemblerSupport<Restaurant, RestaurantSummaryDTO> {

    private final LinksBuilder linksBuilder;

    @Autowired
    private ModelMapper modelMapper;

    public RestaurantSummaryDTOAssembler(LinksBuilder linksBuilder) {
        super(RestaurantController.class, RestaurantSummaryDTO.class);
        this.linksBuilder = linksBuilder;
    }

    @Override
    public RestaurantSummaryDTO toModel(Restaurant restaurant) {
        RestaurantSummaryDTO restaurantSummaryDTO = createModelWithId(restaurant.getId(), restaurant);
        modelMapper.map(restaurant, restaurantSummaryDTO);

        restaurantSummaryDTO.getKitchen().add(linksBuilder.linkToKitchen(restaurantSummaryDTO.getKitchen().getId()));

        return restaurantSummaryDTO;
    }

    @Override
    public CollectionModel<RestaurantSummaryDTO> toCollectionModel(Iterable<? extends Restaurant> entities) {
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
