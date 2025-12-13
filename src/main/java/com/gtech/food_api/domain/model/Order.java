package com.gtech.food_api.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
    private OffsetDateTime createdAt;
    @Column(columnDefinition = "datetime")
    private OffsetDateTime confirmedAt;
    @Column(columnDefinition = "datetime")
    private OffsetDateTime canceledAt;
    @Column(columnDefinition = "datetime")
    private OffsetDateTime deliveredAt;
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();
}
