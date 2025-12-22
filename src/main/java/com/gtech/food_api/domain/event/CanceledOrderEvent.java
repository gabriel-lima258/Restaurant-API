package com.gtech.food_api.domain.event;

import com.gtech.food_api.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CanceledOrderEvent {
    
    private Order order;
}
