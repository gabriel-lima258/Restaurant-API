package com.gtech.food_api.api.V2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;


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
