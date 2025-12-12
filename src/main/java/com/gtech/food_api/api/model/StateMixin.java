package com.gtech.food_api.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import com.gtech.food_api.domain.model.City;

public abstract class StateMixin {
    @JsonIgnore
    private List<City> cities = new ArrayList<>();
}
