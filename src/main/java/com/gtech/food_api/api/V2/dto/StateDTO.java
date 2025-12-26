package com.gtech.food_api.api.V2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "states")
@Getter
@Setter
public class StateDTO extends RepresentationModel<StateDTO> {
    private Long id;
    private String name;
}
