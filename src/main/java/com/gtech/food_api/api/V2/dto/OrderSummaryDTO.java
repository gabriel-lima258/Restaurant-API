package com.gtech.food_api.api.V2.dto;

import com.gtech.food_api.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(name = "OrderSummary", description = "Order summary representation")
@Relation(collectionRelation = "orders")
@Getter
@Setter
public class OrderSummaryDTO extends RepresentationModel<OrderSummaryDTO> {

    @Schema(description = "Order code (UUID)", example = "abc-123-def-456")
    private String code;
    @Schema(description = "Order subtotal", example = "70.00")
    private BigDecimal subtotal;
    @Schema(description = "Shipping fee", example = "5.00")
    private BigDecimal feeShipping;
    @Schema(description = "Total value", example = "75.00")
    private BigDecimal totalValue;
    @Schema(description = "Order status", example = "CONFIRMED")
    private OrderStatus status;
    @Schema(description = "Order creation date", example = "2025-01-01T12:00:00Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Restaurant information")
    private RestaurantSummaryDTO restaurant;
    @Schema(description = "Client information")
    private UserDTO client;
}
