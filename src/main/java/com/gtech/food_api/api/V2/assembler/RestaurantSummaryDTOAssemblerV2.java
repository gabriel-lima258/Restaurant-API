package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.RestaurantControllerV2;
import com.gtech.food_api.api.V2.dto.RestaurantSummaryDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.resource.validations.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class RestaurantSummaryDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Restaurant, RestaurantSummaryDTO> {

    private final LinksBuilderV2 linksBuilder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public RestaurantSummaryDTOAssemblerV2(LinksBuilderV2 linksBuilder) {
        super(RestaurantControllerV2.class, RestaurantSummaryDTO.class);
        this.linksBuilder = linksBuilder;
    }

    @Override
    public RestaurantSummaryDTO toModel(Restaurant restaurant) {
        RestaurantSummaryDTO restaurantSummaryDTO = createModelWithId(restaurant.getId(), restaurant);
        modelMapper.map(restaurant, restaurantSummaryDTO);

        if (usersJwtSecurity.canViewKitchens()) {
            restaurantSummaryDTO.getKitchen().add(linksBuilder.linkToKitchen(restaurantSummaryDTO.getKitchen().getId())); 
        }

        return restaurantSummaryDTO;
    }

    @Override
    public CollectionModel<RestaurantSummaryDTO> toCollectionModel(Iterable<? extends Restaurant> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToRestaurants());
    }

}
