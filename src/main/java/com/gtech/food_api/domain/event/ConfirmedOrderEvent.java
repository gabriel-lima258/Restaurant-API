package com.gtech.food_api.domain.event;

import com.gtech.food_api.domain.model.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Domain Event representando a confirmação de um pedido.
 * 
 * Domain Events são uma parte fundamental do Domain-Driven Design (DDD) que representam
 * algo significativo que aconteceu no domínio da aplicação. Eles são usados para comunicar
 * mudanças de estado entre diferentes partes do sistema de forma desacoplada.
 * 
 * Como funciona na prática:
 * 
 * 1. Ocorrência: Quando um pedido é confirmado, o método confirm() da entidade Order é chamado.
 *    Este método altera o status do pedido e registra este evento usando registerEvent().
 * 
 * 2. Registro do evento: A classe Order estende AbstractAggregateRoot (do Spring Data), que permite
 *    que agregados registrem eventos de domínio. Quando o método confirm() chama registerEvent(),
 *    o evento é adicionado a uma lista interna de eventos pendentes do agregado.
 * 
 * 3. Publicação automática: Quando a entidade Order é salva no repositório (através do método save()),
 *    o Spring Data detecta que há eventos registrados e automaticamente os publica usando o
 *    ApplicationEventPublisher. Isso acontece após o commit da transação, garantindo que os eventos
 *    só sejam disparados se a operação for bem-sucedida.
 * 
 * 4. Reações automáticas: Todos os listeners registrados (como NoticateClientConfirmedOrderListener)
 *    que estão "escutando" este tipo de evento são automaticamente notificados e executam suas
 *    ações específicas (enviar email, atualizar dashboard, etc).
 * 
 * 5. Desacoplamento: O código que confirma o pedido não precisa conhecer ou importar nenhum
 *    serviço de email ou notificação. Ele apenas registra o evento dentro do próprio agregado.
 *    Isso torna o código mais limpo, testável e alinhado com os princípios do DDD, onde o agregado
 *    é responsável por registrar seus próprios eventos de domínio.
 * 
 * 6. Imutabilidade: Domain Events são tipicamente imutáveis (não podem ser alterados após
 *    criados), pois representam um fato histórico que aconteceu em um momento específico.
 * 
 * Este evento específico carrega a entidade Order completa para que os listeners possam
 * acessar todas as informações necessárias (cliente, itens, restaurante, etc) para processar
 * a confirmação do pedido.
 * 
 * Exemplo de uso:
 * 
 * Na entidade Order, no método confirm():
 * 
 *   registerEvent(new ConfirmedOrderEvent(this));
 * 
 * Quando o OrderRepository.save(order) é chamado, o Spring Data automaticamente publica o evento
 * e o NotificateClientConfirmedOrderListener será acionado para enviar o email.
 */
@Getter
@AllArgsConstructor
public class ConfirmedOrderEvent {
    
    private Order order;
}
