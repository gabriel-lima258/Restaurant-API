package com.gtech.food_api.api.V1.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "paymentMethods")
@Getter
@Setter
public class PaymentMethodDTO extends RepresentationModel<PaymentMethodDTO> {
    private Long id;
    private String description;
}
