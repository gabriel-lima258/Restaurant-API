package com.gtech.food_api.api.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "groups")
@Getter
@Setter
public class GroupDTO extends RepresentationModel<GroupDTO> {
    private Long id;
    private String name;
}
