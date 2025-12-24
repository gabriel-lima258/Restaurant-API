package com.gtech.food_api.api.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateDTO extends RepresentationModel<StateDTO> {
    private Long id;
    private String name;
}
