package com.gtech.food_api.api.V2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "paymentMethods")
@Getter
@Setter
public class PaymentMethodDTO extends RepresentationModel<PaymentMethodDTO> {
    private Long id;
    private String description;
}
