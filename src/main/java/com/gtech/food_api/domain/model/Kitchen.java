package com.gtech.food_api.domain.model;

import com.gtech.food_api.core.validation.Groups;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // especificando quais atributos
@Entity
public class Kitchen {

    @NotNull(groups = Groups.KitchenId.class)
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @JsonProperty("title") jackson transforma o nome da propriedade para title no json response
    @NotBlank // por padrao o grupo é default, nao se aplica ao restaurant
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "kitchen")
    private List<Restaurant> restaurants = new ArrayList<>();
}
