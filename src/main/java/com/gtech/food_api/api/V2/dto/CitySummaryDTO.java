package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "CitySummary", description = "City summary representation")
@Relation(collectionRelation = "cities")
@Getter
@Setter
public class CitySummaryDTO extends RepresentationModel<CitySummaryDTO> {
    @Schema(description = "City ID", example = "1")
    private Long id;
    @Schema(description = "City name", example = "São Paulo")
    private String name;
    @Schema(description = "State name", example = "São Paulo")
    private String state;
}
