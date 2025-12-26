package com.gtech.food_api.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "permissions")
@Getter
@Setter
public class PermissionDTO extends RepresentationModel<PermissionDTO> {
    private Long id;
    private String name;
    private String description;
}
