package com.gtech.food_api.api.V2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

// @extends RepresentationModel<CityDTO> para adicionar links HATEOAS.
@Relation(collectionRelation = "cities") // renomeia a relação da coleção para "cities" dos links HATEOAS
@Getter
@Setter
public class CityDTO extends RepresentationModel<CityDTO> {
    private Long id;
    private String name;
    private StateDTO state;
}
