package com.gtech.food_api.domain.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Kitchen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Kitchen kitchen = (Kitchen) o;
        return Objects.equals(id, kitchen.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
