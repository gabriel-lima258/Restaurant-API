package com.gtech.food_api.infra.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.repository.OrderRepositoryQueries;

import java.util.Optional;

/**
 * Implementação da interface OrderRepositoryQueries
 * 
 * Como funciona:
 * - @Repository: Spring detecta e registra automaticamente esta implementação
 * - findByCodeWithEagerType: busca Pedido com base no código usando eager fetch para evitar N+1 queries
 * 
 * Exemplo de uso:
 * Optional<Order> order = orderRepositoryImpl.findByCodeWithEagerType("9f5f3b1f-67e1-4b3a-9f61-c042db67443f");
 * 
 */
@Repository
public class OrderRepositoryImpl implements OrderRepositoryQueries {

    private static final String FIND_BY_CODE_WITH_EAGER_FETCH_QUERY = "SELECT p FROM Order p " +
    "JOIN FETCH p.client " +
    "JOIN FETCH p.restaurant r " +
    "JOIN FETCH r.kitchen " +
    "JOIN FETCH p.deliveryAddress.city c " +
    "JOIN FETCH c.state " +
    "JOIN FETCH p.paymentMethod " +
    "WHERE p.code = :code";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> findByCodeWithEagerType(String code) {
        TypedQuery<Order> query = entityManager
                .createQuery(FIND_BY_CODE_WITH_EAGER_FETCH_QUERY, Order.class)
                .setParameter("code", code);

        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
