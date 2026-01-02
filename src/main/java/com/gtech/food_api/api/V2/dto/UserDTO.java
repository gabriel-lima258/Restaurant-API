package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "User", description = "User representation")
@Relation(collectionRelation = "users")
@Getter
@Setter
public class UserDTO extends RepresentationModel<UserDTO> {
    @Schema(description = "User ID", example = "1")
    private Long id;
    @Schema(description = "User name", example = "John Doe")
    private String name;
    @Schema(description = "User email", example = "john.doe@example.com")
    private String email;
}
