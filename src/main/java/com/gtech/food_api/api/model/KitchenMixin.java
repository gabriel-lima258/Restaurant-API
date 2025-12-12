package com.gtech.food_api.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import com.gtech.food_api.domain.model.Restaurant;

public abstract class KitchenMixin {
    @JsonIgnore // ignorando a serialização do atributo restaurants para evitar loop infinito na busca
    private List<Restaurant> restaurants = new ArrayList<>();
}
