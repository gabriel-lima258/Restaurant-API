package com.gtech.food_api.domain.model;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    CREATED("Created"),
    CONFIRMED("Confirmed", CREATED),
    DELIVERED("Delivered", CONFIRMED),
    CANCELED("Canceled", CREATED, CONFIRMED);

    private String description;
    // lista os status anteriores transitáveis para o status atual
    private List<OrderStatus> previousStatus;

    // OrderStatus... serve para passar um array de status anteriores transitáveis para o status atual
    OrderStatus(String description, OrderStatus... previousStatus) {
        this.description = description;
        this.previousStatus = Arrays.asList(previousStatus);
    }

    public String getDescription() {
        return description;
    }
    public boolean cannotBeAlteratedTo(OrderStatus newStatus) {
        // this é o status atual. Verifica se o status atual está na lista de status anteriores permitidos para o novo status
        return !newStatus.previousStatus.contains(this);
    }
}
