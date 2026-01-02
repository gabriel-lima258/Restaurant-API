package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Schema(name = "Restaurant", description = "Restaurant representation")
@Relation(collectionRelation = "restaurants")
@Getter
@Setter
public class RestaurantDTO extends RepresentationModel<RestaurantDTO> {

    @Schema(description = "Restaurant ID", example = "1")
    private Long id;
    @Schema(description = "Restaurant name", example = "Pizza Palace")
    private String name;
    @Schema(description = "Shipping fee", example = "5.00")
    private BigDecimal shippingFee;
    @Schema(description = "Kitchen type")
    private KitchenDTO kitchen;
    @Schema(description = "Restaurant active status", example = "true")
    private Boolean active;
    @Schema(description = "Restaurant open status", example = "true")
    private Boolean open;
    @Schema(description = "Restaurant address")
    private AddressDTO address;
}
