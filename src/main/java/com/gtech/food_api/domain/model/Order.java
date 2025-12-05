package com.gtech.food_api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Embedded;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // especificando quais atributos
@Entity
public class Order {
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal subtotal;
    @Column(nullable = false)
    private BigDecimal feeShipping;
    @Column(nullable = false)
    private BigDecimal totalValue;

    @Embedded
    private Address deliveryAddress;

    @JsonIgnore
    @CreationTimestamp // data e hora atual
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;
    @Column(columnDefinition = "datetime")
    private LocalDateTime confirmedAt;
    @Column(columnDefinition = "datetime")
    private LocalDateTime canceledAt;
    @Column(columnDefinition = "datetime")
    private LocalDateTime deliveredAt;
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "restaurant_order_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "payment_method_order_id")
    private PaymentMethod paymentMethod;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();
}
