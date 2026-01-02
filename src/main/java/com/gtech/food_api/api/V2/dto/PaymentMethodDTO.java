package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Schema(name = "PaymentMethod", description = "Payment method representation")
@Relation(collectionRelation = "paymentMethods")
@Getter
@Setter
public class PaymentMethodDTO extends RepresentationModel<PaymentMethodDTO> {
    @Schema(description = "Payment method ID", example = "1")
    private Long id;
    @Schema(description = "Payment method description", example = "Credit Card")
    private String description;
}
