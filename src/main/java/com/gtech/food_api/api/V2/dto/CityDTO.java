package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

// @extends RepresentationModel<CityDTO> para adicionar links HATEOAS.
@Schema(name = "City", description = "City representation")
@Relation(collectionRelation = "cities") // renomeia a relação da coleção para "cities" dos links HATEOAS
@Getter
@Setter
public class CityDTO extends RepresentationModel<CityDTO> {
    @Schema(description = "City ID", example = "1")
    private Long id;
    @Schema(description = "City name", example = "São Paulo")
    private String name;
    @Schema(description = "State information")
    private StateDTO state;
}
