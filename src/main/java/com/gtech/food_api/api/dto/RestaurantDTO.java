package com.gtech.food_api.api.dto;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonView;
import com.gtech.food_api.api.dto.view.RestaurantView;

import lombok.Getter;
import lombok.Setter;


@Relation(collectionRelation = "restaurants")
@Getter
@Setter
public class RestaurantDTO extends RepresentationModel<RestaurantDTO> {

    @JsonView(RestaurantView.Summary.class)
    private Long id;

    @JsonView(RestaurantView.Summary.class)
    private String name;

    @JsonView(RestaurantView.Summary.class)
    private BigDecimal shippingFee;

    @JsonView(RestaurantView.Summary.class)
    private KitchenDTO kitchen;
    
    private Boolean active;
    private Boolean open;
    private AddressDTO address;
}
