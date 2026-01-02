package com.gtech.food_api.api.V2.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.gtech.food_api.api.V2.dto.view.RestaurantView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "Kitchen", description = "Kitchen type representation")
@Relation(collectionRelation = "kitchens")
@Getter
@Setter
public class KitchenDTO extends RepresentationModel<KitchenDTO> {

    @Schema(description = "Kitchen ID", example = "1")
    @JsonView(RestaurantView.Summary.class)
    private Long id;

    @Schema(description = "Kitchen name", example = "Italian")
    @JsonView(RestaurantView.Summary.class)
    private String name;
}
