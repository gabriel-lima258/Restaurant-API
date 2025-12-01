package com.gtech.food_api.infrastructure.repository;

import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.repository.RestaurantRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// classe customizada para implementar a interface por métodos JPA, vantagem -> usar codigo java e codigo dinamico
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    // usando o criteria api para fazer a query complexas e dinamicas
    @Override
    public List<Restaurant> find(String name, BigDecimal minShippingFee, BigDecimal maxShippingFee) {
        var builder = entityManager.getCriteriaBuilder();

        // construtor para busca de Restaurant class
        var criteria = builder.createQuery(Restaurant.class);
        var root = criteria.from(Restaurant.class); // from Restaurant

        // coleta os predicados da query, nome, minShippingFee e maxShippingFee, criando uma list
        var predicates = new ArrayList<Predicate>();

        // If not null or empty
        if (StringUtils.hasLength(name)) {
            predicates.add(builder.like(root.get("name"), "%" + name + "%"));
        }

        if (minShippingFee != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("shippingFee"), minShippingFee));
        }

        if (maxShippingFee != null) {
            predicates.add((builder.lessThanOrEqualTo(root.get("shippingFee"), maxShippingFee)));
        }

        // criando um where com os predicados, recebe um array, então deve ser convertido para Predicate[]
        criteria.where(predicates.toArray(Predicate[]::new));

        // criando a query com o criteria
        var query = entityManager.createQuery(criteria);
        return query.getResultList(); // retorna uma lista de Restaurant com as queries
    }
}
