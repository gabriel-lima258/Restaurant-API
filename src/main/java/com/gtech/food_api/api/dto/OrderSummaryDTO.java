package com.gtech.food_api.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.gtech.food_api.domain.model.OrderStatus;

@Getter
@Setter
public class OrderSummaryDTO {

    private String code;
    private BigDecimal subtotal;
    private BigDecimal feeShipping;
    private BigDecimal totalValue;
    private OrderStatus status;
    private OffsetDateTime createdAt;

    private RestaurantSummaryDTO restaurant;
    private UserDTO client;
}
