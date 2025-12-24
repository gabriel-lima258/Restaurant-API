package com.gtech.food_api.domain.model;

import java.util.Arrays;
import java.util.List;

/**
 * Diagrama de transições permitidas:
 * 
 *     CREATED
 *       ├──> CONFIRMED (pode vir de CREATED)
 *       │      └──> DELIVERED (pode vir de CONFIRMED)
 *       │
 *       └──> CANCELED (pode vir de CREATED ou CONFIRMED)
 * 
 * Regras de transição:
 * - CREATED: status inicial, não pode vir de nenhum outro status
 * - CONFIRMED: só pode vir de CREATED
 * - DELIVERED: só pode vir de CONFIRMED
 * - CANCELED: pode vir de CREATED ou CONFIRMED (mas não de DELIVERED)
 */
public enum OrderStatus {

    CREATED("Created"),
    CONFIRMED("Confirmed", CREATED),
    DELIVERED("Delivered", CONFIRMED),
    CANCELED("Canceled", CREATED, CONFIRMED);

    private String description;
    
    /**
     * Lista dos status anteriores que permitem transição para este status.
     * Exemplo: CANCELED.previousStatus = [CREATED, CONFIRMED]
     *          Significa que é possível cancelar de CREATED ou CONFIRMED
     */
    private List<OrderStatus> previousStatus;

    /**
     * Construtor do enum que define a descrição e os status anteriores permitidos.
     * 
     * OrderStatus... é um varargs (variable arguments), permitindo passar zero ou mais
     * status anteriores. Isso permite flexibilidade:
     * - CREATED não tem status anteriores (array vazio)
     * - CONFIRMED tem um status anterior (CREATED)
     * - CANCELED tem dois status anteriores (CREATED, CONFIRMED)
     * 
     * @param description Descrição legível do status
     * @param previousStatus Lista de status anteriores que permitem transição para este status
     */
    OrderStatus(String description, OrderStatus... previousStatus) {
        this.description = description;
        // Converte o array varargs em uma List para facilitar operações como contains()
        this.previousStatus = Arrays.asList(previousStatus);
    }

    public String getDescription() {
        return description;
    }
    
    /**
     * Verifica se NÃO é possível alterar do status atual para o novo status.
     * 
     * Lógica:
     * - Pega o novo status desejado
     * - Verifica se o status atual (this) está na lista de status anteriores permitidos do novo status
     * - Se NÃO estiver na lista, retorna true (não pode alterar)
     * - Se estiver na lista, retorna false (pode alterar)
     * 
     * Exemplo:
     * - CREATED.cannotBeAlteratedTo(CONFIRMED) → false (pode alterar, CREATED está na lista)
     * - CREATED.cannotBeAlteratedTo(DELIVERED) → true (não pode alterar, CREATED não está na lista)
     * - CONFIRMED.cannotBeAlteratedTo(CANCELED) → false (pode alterar, CONFIRMED está na lista)
     * - DELIVERED.cannotBeAlteratedTo(CANCELED) → true (não pode alterar, DELIVERED não está na lista)
     * 
     * @param newStatus Novo status desejado
     * @return true se NÃO pode alterar, false se pode alterar
     */
    public boolean cannotBeAlteratedTo(OrderStatus newStatus) {
        // this é o status atual. Verifica se o status atual está na lista de status anteriores permitidos para o novo status
        // Se não estiver na lista, significa que não pode fazer essa transição
        return !newStatus.previousStatus.contains(this);
    }

    /**
     * Verifica se É possível alterar do status atual para o novo status.
     * 
     * Este é um método de conveniência que simplesmente inverte o resultado
     * de cannotBeAlteratedTo() para tornar o código mais legível.
     * 
     * Exemplo de uso:
     * if (order.getStatus().canBeAlteratedTo(OrderStatus.CONFIRMED)) {
     *     order.setStatus(OrderStatus.CONFIRMED);
     * }
     */
    public boolean canBeAlteratedTo(OrderStatus newStatus) {
        return !cannotBeAlteratedTo(newStatus);
    }
}
