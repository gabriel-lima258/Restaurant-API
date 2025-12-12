package com.gtech.food_api.api.model;

import java.lang.Thread.State;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public abstract class CityMixin {
    @JsonIgnoreProperties(value = "name", allowGetters = true)
    private State state;
}
