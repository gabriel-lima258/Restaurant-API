package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.model.OrderStatus;
import com.gtech.food_api.domain.service.exceptions.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatusOrderService {

    @Autowired
    private OrderService orderService;


    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = orderService.findOrFail(orderId);
        order.canBeConfirmed();
    }

    @Transactional
    public void deliverOrder(Long orderId) {
        Order order = orderService.findOrFail(orderId);
        order.canBeDelivered();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderService.findOrFail(orderId);
        order.canBeCanceled();
    }

}
