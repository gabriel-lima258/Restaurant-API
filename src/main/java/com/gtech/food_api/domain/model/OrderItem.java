package com.gtech.food_api.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // especificando quais atributos
@Entity
public class OrderItem {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String observation;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // calcular o total do item
    public void calculateTotalPrice() {
        BigDecimal unitPrice = getUnitPrice();
        Integer quantity = getQuantity();

        // se o unitPrice for nulo, seta para 0
        if (unitPrice == null) {
            unitPrice = BigDecimal.ZERO;
        }
        // se a quantidade for nula, seta para 0
        if (quantity == null) {
            quantity = 0;
        }
        // totalPrice = unitPrice * quantity
        this.totalPrice = unitPrice.multiply(new BigDecimal(quantity));
    }
}
