package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Schema(name = "Product", description = "Product representation")
@Relation(collectionRelation = "products")
@Getter
@Setter
public class ProductDTO extends RepresentationModel<ProductDTO> {
    @Schema(description = "Product ID", example = "1")
    private Long id;
    @Schema(description = "Product name", example = "Pizza Margherita")
    private String name;
    @Schema(description = "Product description", example = "Delicious pizza with tomato, mozzarella and basil")
    private String description;
    @Schema(description = "Product price", example = "35.00")
    private BigDecimal price;
    @Schema(description = "Product active status", example = "true")
    private Boolean active;
}
