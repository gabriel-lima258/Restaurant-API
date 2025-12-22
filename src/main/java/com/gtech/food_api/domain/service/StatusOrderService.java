package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatusOrderService {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void confirmOrder(String orderCode) {
        Order order = orderService.findOrFail(orderCode);
        order.confirm();
        // para disparar o evento de confirmação de pedido precisa salvar o pedido 
        orderRepository.save(order);
    }

    @Transactional
    public void deliverOrder(String orderCode) {
        Order order = orderService.findOrFail(orderCode);
        order.deliver();
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(String orderCode) {
        Order order = orderService.findOrFail(orderCode);
        order.cancel();
        orderRepository.save(order);
    }

}
