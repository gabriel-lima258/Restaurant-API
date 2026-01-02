package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Schema(name = "OrderItem", description = "Order item representation")
@Getter
@Setter
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {

    @Schema(description = "Product ID", example = "1")
    private Long productId;
    @Schema(description = "Product name", example = "Pizza Margherita")
    private String productName;
    @Schema(description = "Item quantity", example = "2")
    private Integer quantity;
    @Schema(description = "Unit price", example = "35.00")
    private BigDecimal unitPrice;
    @Schema(description = "Total price", example = "70.00")
    private BigDecimal totalPrice;
    @Schema(description = "Item observation", example = "No onions please")
    private String observation;
}
