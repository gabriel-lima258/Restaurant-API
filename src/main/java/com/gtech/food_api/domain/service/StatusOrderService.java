package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.model.OrderStatus;
import com.gtech.food_api.domain.service.email.SendEmailService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatusOrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SendEmailService sendEmailService;

    @Transactional
    public void confirmOrder(String orderCode) {
        Order order = orderService.findOrFail(orderCode);
        
        order.confirm();

        var message = SendEmailService.Message.builder()
        .subject(order.getClient().getName() + " - Pedido confirmado")
        .body("O pedido <strong>" + order.getCode() + "</strong> foi confirmado")
        .recipient(order.getClient().getEmail())
        .build();
        sendEmailService.send(message);

        System.out.println("Email enviado para: " + message.getRecipients());
    }

    @Transactional
    public void deliverOrder(String orderCode) {
        Order order = orderService.findOrFail(orderCode);
        order.deliver();
    }

    @Transactional
    public void cancelOrder(String orderCode) {
        Order order = orderService.findOrFail(orderCode);
        order.cancel();
    }

}
