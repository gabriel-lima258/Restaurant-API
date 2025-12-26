package com.gtech.food_api.api.dto;

import java.math.BigDecimal;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Getter;
import lombok.Setter;

@Relation(collectionRelation = "products")
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
}
