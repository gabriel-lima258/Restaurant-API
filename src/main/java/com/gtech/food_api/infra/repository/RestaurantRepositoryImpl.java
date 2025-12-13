package com.gtech.food_api.infra.repository;

import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.repository.RestaurantRepository;
import com.gtech.food_api.domain.repository.RestaurantRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.gtech.food_api.infra.repository.specification.RestaurantFreeFeeSpec.withFreeFee;
import static com.gtech.food_api.infra.repository.specification.RestaurantFreeFeeSpec.withSimiliarName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface RestaurantRepositoryQueries
 * 
 * Como funciona:
 * - @Repository: Spring detecta e registra automaticamente esta implementação
 * - find: busca Restaurantes com base em nome, frete mínimo e frete máximo
 * - findWithFreeFee: busca Restaurantes com frete grátis e nome similar
 * 
 * Exemplo de uso:
 * List<Restaurant> restaurants = restaurantRepositoryImpl.find("Free shipping", null, null);
 * 
 * Vantagens:
 * - Usamos o criteria api para fazer a query complexas e dinamicas
 * - Usamos o specification api para fazer a query complexas e dinamicas
 */
@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired @Lazy // instancia somente quando for preciso, evita dependencia circular
    private RestaurantRepository restaurantRepository;

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

    @Override
    public List<Restaurant> findWithFreeFee(String name) {
        return restaurantRepository.findAll(withFreeFee().and(withSimiliarName(name)));
    }
}
