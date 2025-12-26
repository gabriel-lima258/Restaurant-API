package com.gtech.food_api.api.V1.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonView;
import com.gtech.food_api.api.V1.dto.view.RestaurantView;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "kitchens")
@Getter
@Setter
public class KitchenDTO extends RepresentationModel<KitchenDTO> {

    @JsonView(RestaurantView.Summary.class)
    private Long id;

    @JsonView(RestaurantView.Summary.class)
    private String name;
}
