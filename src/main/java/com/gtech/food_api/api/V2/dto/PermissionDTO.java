package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "Permission", description = "Permission representation")
@Relation(collectionRelation = "permissions")
@Getter
@Setter
public class PermissionDTO extends RepresentationModel<PermissionDTO> {
    @Schema(description = "Permission ID", example = "1")
    private Long id;
    @Schema(description = "Permission name", example = "READ_RESTAURANTS")
    private String name;
    @Schema(description = "Permission description", example = "Read restaurants")
    private String description;
}
