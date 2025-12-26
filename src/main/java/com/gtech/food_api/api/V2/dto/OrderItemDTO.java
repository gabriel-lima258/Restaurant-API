package com.gtech.food_api.api.V1.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class OrderItemDTO extends RepresentationModel<OrderItemDTO> {

    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String observation;
}
