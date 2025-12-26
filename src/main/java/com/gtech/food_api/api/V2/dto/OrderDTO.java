package com.gtech.food_api.api.V1.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.gtech.food_api.domain.model.OrderStatus;

@Relation(collectionRelation = "orders")
@Getter
@Setter
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private String code;
    private BigDecimal subtotal;
    private BigDecimal feeShipping;
    private BigDecimal totalValue;
    private OrderStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime confirmedAt;
    private OffsetDateTime canceledAt;
    private OffsetDateTime deliveredAt;

    private RestaurantSummaryDTO restaurant;
    private UserDTO client;
    private PaymentMethodDTO paymentMethod;
    private AddressDTO deliveryAddress;
    private List<OrderItemDTO> items;
}
