package com.gtech.food_api.api.V1.dto;

import org.springframework.hateoas.RepresentationModel;

import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;   
import lombok.Setter;

// @extends RepresentationModel<CityDTO> para adicionar links HATEOAS.
@Relation(collectionRelation = "cities") // renomeia a relação da coleção para "cities" dos links HATEOAS
@Getter
@Setter
public class CityDTO extends RepresentationModel<CityDTO> {
    private Long id;
    private String name;
    private StateDTO state;
}
