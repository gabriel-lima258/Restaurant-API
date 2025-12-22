package com.gtech.food_api.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Embedded;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.gtech.food_api.domain.event.CanceledOrderEvent;
import com.gtech.food_api.domain.event.ConfirmedOrderEvent;
import com.gtech.food_api.domain.event.DeliveredOrderEvent;
import com.gtech.food_api.domain.service.exceptions.BusinessException;

import lombok.Data;
import lombok.EqualsAndHashCode;

// AbstractAggregateRoot é uma classe que permite adicionar eventos ao pedido
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // especificando quais atributos
@Entity
public class Order extends AbstractAggregateRoot<Order>{
    
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code; // code será um UUID, evita expor do id do pedido publicamente
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

    public void confirm() {
        setStatus(OrderStatus.CONFIRMED);
        setConfirmedAt(OffsetDateTime.now());
        // dispara o evento de confirmação de pedido passando referência a esta classe Order
        registerEvent(new ConfirmedOrderEvent(this));
    }

    public void deliver() {
        setStatus(OrderStatus.DELIVERED);
        setDeliveredAt(OffsetDateTime.now());
        registerEvent(new DeliveredOrderEvent(this));
    }

    public void cancel() {
        setStatus(OrderStatus.CANCELED);
        setCanceledAt(OffsetDateTime.now());
        registerEvent(new CanceledOrderEvent(this));
    }

    // validação feita na entidade para evitar que o status seja alterado para um status inválido
    private void setStatus(OrderStatus newStatus) {
        // Verifica se o status já é o mesmo que está sendo tentado definir
        if (getStatus().equals(newStatus)) {
            throw new BusinessException(String.format("Order %s is already %s", getCode(), newStatus.getDescription()));
        }
        
        // Verifica se a transição de status é válida
        if (getStatus().cannotBeAlteratedTo(newStatus)) {
            throw new BusinessException(String.format("Order %s cannot be altered to %s because it is in status %s", getCode(), newStatus.getDescription(), getStatus().getDescription()));
        }
        this.status = newStatus;
    }

    // gera o código do pedido quando o pedido é salvo, evita o erro de null
    @PrePersist
    private void generateCode() {
        setCode(UUID.randomUUID().toString());
    }
}
