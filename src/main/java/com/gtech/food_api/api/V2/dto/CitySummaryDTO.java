package com.gtech.food_api.api.V1.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "cities")
@Getter
@Setter
public class CitySummaryDTO extends RepresentationModel<CitySummaryDTO> {
    private Long id;
    private String name;
    private String state;
}
