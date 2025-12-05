package com.gtech.food_api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurant {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal shippingFee;

    @JsonIgnore
    @CreationTimestamp // data e hora atual
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;
    @JsonIgnore
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @Embedded // indica que address é um objeto de restaurant
    private Address address;

    @ManyToOne // por padrão é eager loading, busca no banco mesmo sem precisar
    @JoinColumn(name = "kitchen_id")
    private Kitchen kitchen;

    @JsonIgnore // ignorando o relacionamento payment ao buscar o restaurant
    @ManyToMany // por padrão é lazy loading, busca no banco por demanda
    @JoinTable(name = "restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    List<Product> products = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();
}
