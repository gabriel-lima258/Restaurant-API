package com.gtech.food_api.infra.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gtech.food_api.api.dto.report.DailySelling;
import com.gtech.food_api.domain.filter.DailySellingFilter;
import com.gtech.food_api.domain.service.SellingQueryService;
import com.gtech.food_api.domain.model.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;

@Repository
public class SellingQueryServiceImpl implements SellingQueryService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DailySelling> queryDailySelling(DailySellingFilter filter) {
        // cria o builder para criar a query
        var builder = entityManager.getCriteriaBuilder();
        // cria a query para a classe DailySelling
        var query = builder.createQuery(DailySelling.class);
        // cria a classe root para a query FROM
        var root = query.from(Order.class);

        // criando uma lista de predicados para a query
        var predicates = new ArrayList<Predicate>();

        if (filter.getRestaurantId() != null) {
            predicates.add(builder.equal(root.get("restaurant").get("id"), filter.getRestaurantId()));
        }

        if (filter.getCreationDateStart() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreationDateStart()));
        }

        if (filter.getCreationDateEnd() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), filter.getCreationDateEnd()));
        }

        // criando um where com os predicados, recebe um array, então deve ser convertido para Predicate[]
        query.where(predicates.toArray(Predicate[]::new));

        // criando um extrator de data 
        var functionDateCreatedAt = builder.function(
            "DATE", LocalDate.class, root.get("createdAt"));

        // criando o select para o construct de DailySelling, usando o root de Order
        /*
        SELECT
            DATE(createdAt) AS date,
            COUNT(id) AS totalSales,
            SUM(totalAmount) AS totalBilling
        FROM
            Order
        WHERE
            createdAt BETWEEN :creationDateStart AND :creationDateEnd
        GROUP BY
            DATE(createdAt)
        */
        var selection = builder.construct(DailySelling.class, 
            functionDateCreatedAt,
            builder.count(root.get("id")),
            builder.sum(root.get("totalValue"))
        );
        query.select(selection);
        // agrupando por data de criação
        query.groupBy(functionDateCreatedAt);

        // executando a query e retornando o resultado como lista de DailySelling
        return entityManager.createQuery(query).getResultList();
    }
}
