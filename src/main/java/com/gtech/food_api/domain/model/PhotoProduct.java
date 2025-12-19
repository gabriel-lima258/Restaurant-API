package com.gtech.food_api.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class PhotoProduct {
    
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "product_id")
    private Long id;

    private String fileName;
    private String description;
    private String contentType;
    private Long size;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // mapeia o id do product para o id da photo product, isso permite nao mapear as entidades
    private Product product;

    public Long getRestaurantId() {
        if (getProduct() != null) {
            return getProduct().getRestaurant().getId();
        }
        return null;
    }

    public Long getProductId() {
        if (getProduct() != null) {
            return getProduct().getId();
        }
        return null;
    }
}
