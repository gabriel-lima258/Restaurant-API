package com.gtech.food_api.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.gtech.food_api.domain.event.CanceledOrderEvent;
import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.service.email.SendEmailService;

@Component
public class NotificateClientCanceledOrderListener {

    @Autowired
    private SendEmailService sendEmailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCanceledOrder(CanceledOrderEvent event) {
        // Extrai o pedido do evento
        Order order = event.getOrder();
        
        // Monta a mensagem de email com os dados do pedido
        var message = SendEmailService.Message.builder()
        .subject(order.getClient().getName() + " - Pedido cancelado")
        .body("canceled-order.html") // escolhe o template de email
        .variable("order", order) // passa as variáveis para o template
        .recipient(order.getClient().getEmail())
        .build();
        
        // Envia o email para o cliente
        sendEmailService.send(message);
    }
    
}
