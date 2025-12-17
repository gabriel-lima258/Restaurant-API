package com.gtech.food_api.domain.repository;

import java.util.Optional;

import com.gtech.food_api.domain.model.Order;

/*
 * - findByCodeWithEagerType: busca Pedido com base no código usando eager fetch para evitar N+1 queries
 * 
 * Exemplo de uso:
 * Optional<Order> order = orderRepositoryQueries.findByCodeWithEagerType("9f5f3b1f-67e1-4b3a-9f61-c042db67443f");
 * 
 * Classe de implementação: OrderRepositoryImpl
 */
public interface OrderRepositoryQueries {

    // resolve N+1 queries
    Optional<Order> findByCodeWithEagerType(String code);
}
