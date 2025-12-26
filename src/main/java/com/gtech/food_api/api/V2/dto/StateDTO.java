package com.gtech.food_api.api.V1.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "states")
@Getter
@Setter
public class StateDTO extends RepresentationModel<StateDTO> {
    private Long id;
    private String name;
}
