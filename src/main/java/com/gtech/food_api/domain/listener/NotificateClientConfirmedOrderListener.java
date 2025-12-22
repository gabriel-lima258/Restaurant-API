package com.gtech.food_api.domain.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.gtech.food_api.domain.event.ConfirmedOrderEvent;
import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.service.email.SendEmailService;

/**
 * Listener responsável por notificar o cliente quando um pedido é confirmado.
 * 
 * Esta classe implementa o padrão Observer/Listener do Spring Framework, onde ela se "inscreve"
 * para escutar eventos específicos que acontecem na aplicação. Quando o evento ConfirmedOrderEvent
 * é publicado após o commit de uma transação, este listener é automaticamente acionado usando
 * @TransactionalEventListener com fase AFTER_COMMIT, garantindo que o email só seja enviado se
 * o pedido foi realmente confirmado e salvo no banco de dados.
 * 
 * Como funciona o fluxo completo:
 * 
 * 1. Na entidade Order, quando o método confirm() é chamado, ele registra um ConfirmedOrderEvent
 *    usando registerEvent() (herdado do AbstractAggregateRoot do Spring Data).
 * 
 * 2. Quando o OrderRepository.save(order) é executado, o Spring Data detecta que há eventos
 *    registrados no agregado e automaticamente os publica usando o ApplicationEventPublisher,
 *    mas apenas após o commit bem-sucedido da transação.
 * 
 * 3. Após o commit bem-sucedido da transação, o Spring Framework detecta que existe um listener
 *    (@TransactionalEventListener) registrado para esse tipo de evento e automaticamente chama
 *    o método onConfirmedOrder() desta classe, mas apenas se a transação foi commitada com sucesso.
 * 
 * 4. Este método recebe o evento contendo o pedido confirmado, extrai as informações necessárias
 *    (como email do cliente e dados do pedido) e monta uma mensagem de email.
 * 
 * 5. A mensagem é enviada ao cliente através do SendEmailService, usando um template HTML
 *    pré-configurado (confirmed-order.html) que será preenchido com os dados do pedido.
 * 
 * Vantagens desta abordagem:
 * 
 * - Desacoplamento: O código que confirma o pedido não precisa saber que precisa enviar email.
 *   O agregado apenas registra o evento e outros componentes reagem como desejarem.
 * 
 * - Alinhamento com DDD: O agregado (Order) é responsável por registrar seus próprios eventos
 *   de domínio, mantendo a lógica de negócio encapsulada dentro da entidade.
 * 
 * - Extensibilidade: Se no futuro precisarmos adicionar outras ações quando um pedido é confirmado
 *   (ex: atualizar dashboard, enviar SMS, notificar cozinha), basta criar novos listeners sem
 *   modificar o código que confirma o pedido.
 * 
 * - Testabilidade: Podemos testar o envio de email isoladamente, mockando o evento.
 */
@Component
public class NotificateClientConfirmedOrderListener {

    @Autowired
    private SendEmailService sendEmailService;

    /**
     * Método que é executado automaticamente sempre que um ConfirmedOrderEvent é publicado.
     * 
     * A anotação @TransactionalEventListener é uma versão especializada do @EventListener que
     * permite controlar quando o listener será executado em relação ao ciclo de vida da transação.
     * 
     * O parâmetro phase = TransactionPhase.AFTER_COMMIT garante que este método só será executado
     * APÓS o commit bem-sucedido da transação que salvou o pedido. Isso é crucial porque:
     * 
     * - Garantia de consistência: O email só será enviado se o pedido foi realmente confirmado
     *   e salvo no banco de dados. Se a transação falhar (rollback), o email não será enviado,
     *   evitando notificar o cliente sobre um pedido que não foi realmente confirmado.
     * 
     * - Isolamento de transação: O listener executa em uma nova transação separada, então se
     *   o envio do email falhar, isso não afetará a transação que confirmou o pedido.
     * 
     * - Ordem de execução: O AFTER_COMMIT garante que todos os dados já estão persistidos e
     *   visíveis para outras transações antes de executar ações secundárias como envio de email. Se falhar listerner fica por isso, por que ja confirmou no banco de dados.
     * 
     * Quando o OrderRepository.save(order) é executado e a transação é commitada com sucesso,
     * o Spring Data publica os eventos registrados no AbstractAggregateRoot e este método
     * é automaticamente acionado.
     * 
     * @param event O evento contendo o pedido que foi confirmado
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onConfirmedOrder(ConfirmedOrderEvent event) {
        // Extrai o pedido do evento
        Order order = event.getOrder();
        
        // Monta a mensagem de email com os dados do pedido
        var message = SendEmailService.Message.builder()
        .subject(order.getClient().getName() + " - Pedido confirmado")
        .body("confirmed-order.html") // escolhe o template de email
        .variable("order", order) // passa as variáveis para o template
        .recipient(order.getClient().getEmail())
        .build();
        
        // Envia o email para o cliente
        sendEmailService.send(message);
    }
    
}
