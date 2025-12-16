package com.gtech.food_api.domain.model;

import com.gtech.food_api.core.validation.ValueZeroIncludedDescription;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validação aplicada: Se shippingFee = 0, então o name DEVE conter "Free shipping".
 * Caso contrário, a validação falhará ao salvar/atualizar o restaurante.
 */
@ValueZeroIncludedDescription(valueField = "shippingFee", descriptionField = "name", requiredDescription = "Free shipping")
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

    // @Multiple(number = 5)
    @Column(nullable = false)
    private BigDecimal shippingFee;

    // offsetDateTime é a data e hora atual com timezone UTC (GMT+0), universalmente aceito
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime updatedAt;

    private Boolean active = Boolean.TRUE;

    private Boolean open = Boolean.TRUE;

    @Embedded // indica que address é um atributo de outra entidade incorporada
    private Address address;

    //@ConvertGroup(from = Default.class, to = Groups.KitchenId.class)
    @ManyToOne // por padrão é eager loading, busca no banco mesmo sem precisar
    @JoinColumn(name = "kitchen_id")
    private Kitchen kitchen;

    @ManyToMany // por padrão é lazy loading, busca no banco por demanda
    @JoinTable(name = "restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();

    public void activate() {
        setActive(true);
    }

    public void deactivate() {
        setActive(false);
    }

    public void openRestaurant() {
        setOpen(true);
    }

    public void closedRestaurant() {
        setOpen(false);
    }

    public boolean disassociatePaymentMethod(PaymentMethod paymentMethod) {
        return getPaymentMethods().remove(paymentMethod);
    }

    public boolean associatePaymentMethod(PaymentMethod paymentMethod) {
        return getPaymentMethods().add(paymentMethod);
    }
    
}
