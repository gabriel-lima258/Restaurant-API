package com.gtech.food_api.api.V1.dto;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;


@Relation(collectionRelation = "restaurants")
@Getter
@Setter
public class RestaurantDTO extends RepresentationModel<RestaurantDTO> {

    private Long id;
    private String name;
    private BigDecimal shippingFee;
    private KitchenDTO kitchen;
    
    private Boolean active;
    private Boolean open;
    private AddressDTO address;
}
