package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "State", description = "State representation")
@Relation(collectionRelation = "states")
@Getter
@Setter
public class StateDTO extends RepresentationModel<StateDTO> {
    @Schema(description = "State ID", example = "1")
    private Long id;
    @Schema(description = "State name", example = "São Paulo")
    private String name;
}
