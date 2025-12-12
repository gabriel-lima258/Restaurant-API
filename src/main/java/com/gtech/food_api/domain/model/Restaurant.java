package com.gtech.food_api.domain.model;

import com.gtech.food_api.core.validation.Groups;
import com.gtech.food_api.core.validation.ValueZeroIncludedDescription;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @NotBlank
    private String name;

    // @Multiple(number = 5)
    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private BigDecimal shippingFee;

    @CreationTimestamp // data e hora atual
    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private LocalDateTime updatedAt;

    @Embedded // indica que address é um atributo de outra entidade incorporada
    private Address address;

    @Valid // validacao em cascata, valida a kitchen do restaurant
    @ConvertGroup(from = Default.class, to = Groups.KitchenId.class)
    @NotNull
    @ManyToOne // por padrão é eager loading, busca no banco mesmo sem precisar
    @JoinColumn(name = "kitchen_id")
    private Kitchen kitchen;

    @ManyToMany // por padrão é lazy loading, busca no banco por demanda
    @JoinTable(name = "restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Order> orders = new ArrayList<>();
}
