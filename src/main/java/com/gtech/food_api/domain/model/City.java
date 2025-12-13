package com.gtech.food_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class City {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;

    // @ConvertGroup(from = Default.class, to = Groups.StateId.class)
    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;
}
