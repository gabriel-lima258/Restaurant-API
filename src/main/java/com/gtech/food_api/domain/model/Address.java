package com.gtech.food_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

// classe de componente de restaurant, ao inves de criar todos os atributos diretamente na classe Restaurant
// podemos criar uma classe Address e relacionar os atributos diretamente na classe Restaurant
@Data
@Embeddable // embeddable = atributo de outra entidade incorporada
public class Address {

    @Column(name = "address_cep")
    private String cep;
    @Column(name = "address_public_place")
    private String publicPlace;
    @Column(name = "address_number")
    private String number;
    @Column(name = "address_complement")
    private String complement;
    @Column(name = "address_neighborhood")
    private String neighborhood;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_city_id")
    private City city;
}
