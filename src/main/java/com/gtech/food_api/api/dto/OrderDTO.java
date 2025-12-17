package com.gtech.food_api.api.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import com.gtech.food_api.domain.model.OrderStatus;

@Getter
@Setter
public class OrderDTO {

    private Long id;
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
