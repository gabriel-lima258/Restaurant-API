package com.gtech.food_api.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private BigDecimal subtotal;
    private BigDecimal feeShipping;
    private BigDecimal totalValue;

    // enum string para que o status seja salvo como string no banco de dados
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @CreationTimestamp // data e hora atual
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private OffsetDateTime createdAt;
    @Column(columnDefinition = "datetime")
    private OffsetDateTime confirmedAt;
    @Column(columnDefinition = "datetime")
    private OffsetDateTime canceledAt;
    @Column(columnDefinition = "datetime")
    private OffsetDateTime deliveredAt;

    @Embedded
    private Address deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    // CascadeType.ALL: quando deletar o pedido, deletar também os itens do pedido
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    public void calculateTotalValue() {
        // calcular o total de cada item
        getItems().forEach(OrderItem::calculateTotalPrice);

        // calcular o subtotal dos items feito no calculateTotalPrice do OrderItem
        this.subtotal = getItems().stream()
        .map(OrderItem::getTotalPrice) 
        .reduce(BigDecimal.ZERO, BigDecimal::add);

        // totalValue = subtotal + feeShipping
        this.totalValue = this.subtotal.add(this.feeShipping);
    }
}
