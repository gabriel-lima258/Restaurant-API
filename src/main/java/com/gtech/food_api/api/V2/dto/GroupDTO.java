package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "Group", description = "User group representation")
@Relation(collectionRelation = "groups")
@Getter
@Setter
public class GroupDTO extends RepresentationModel<GroupDTO> {
    @Schema(description = "Group ID", example = "1")
    private Long id;
    @Schema(description = "Group name", example = "Administrators")
    private String name;
}
