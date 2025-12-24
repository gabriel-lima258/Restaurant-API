package com.gtech.food_api.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.gtech.food_api.domain.model.OrderStatus;

@Relation(collectionRelation = "orders")
@Getter
@Setter
public class OrderSummaryDTO extends RepresentationModel<OrderSummaryDTO> {

    private String code;
    private BigDecimal subtotal;
    private BigDecimal feeShipping;
    private BigDecimal totalValue;
    private OrderStatus status;
    private OffsetDateTime createdAt;

    private RestaurantSummaryDTO restaurant;
    private UserDTO client;
}
