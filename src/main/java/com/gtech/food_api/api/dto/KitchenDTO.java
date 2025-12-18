package com.gtech.food_api.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.gtech.food_api.api.dto.view.RestaurantView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KitchenDTO {

    @JsonView(RestaurantView.Summary.class)
    private Long id;

    @JsonView(RestaurantView.Summary.class)
    private String name;
}
