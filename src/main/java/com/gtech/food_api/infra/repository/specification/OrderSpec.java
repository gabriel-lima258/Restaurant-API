package com.gtech.food_api.infra.repository.specification;

import java.util.ArrayList;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.gtech.food_api.domain.filter.OrderFilter;
import com.gtech.food_api.domain.model.Order;

public class OrderSpec {
    public static Specification<Order> withFilter(OrderFilter filter) {
        return ((root, query, builder) -> {
            // usando fetch para carregar as relações com eager loading
            // evitando o problema de N+1
            root.fetch("client");
            root.fetch("restaurant").fetch("kitchen");

            // criando uma lista de predicates, para adicionar as condições do filtro
            var predicates = new ArrayList<Predicate>();

            if (filter.getClientId() != null) {
                // comparando o ID do cliente, não o objeto cliente
                predicates.add(builder.equal(root.get("client").get("id"), filter.getClientId()));
            }

            if (filter.getRestaurantId() != null) {
                // comparando o ID do restaurante, não o objeto restaurante
                predicates.add(builder.equal(root.get("restaurant").get("id"), filter.getRestaurantId()));
            }

            if (filter.getCreationDateStart() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreationDateStart()));
            }

            if (filter.getCreationDateEnd() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), filter.getCreationDateEnd()));
            }

            // retornando a lista de predicates concatenados com o operador AND
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
