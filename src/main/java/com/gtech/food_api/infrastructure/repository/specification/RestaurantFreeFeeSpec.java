package com.gtech.food_api.infrastructure.repository.specification;

import com.gtech.food_api.domain.model.Restaurant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

// factory de specification
// specification é um padrão de projeto para filtros reutilizaveis, que comunica com JPA
public class RestaurantFreeFeeSpec {

    // criando um predicate de free shipping
    public static Specification<Restaurant> withFreeFee() {
        return ((root, query, builder) ->
                builder.equal(root.get("shippingFee"), BigDecimal.ZERO));
    }

    public static Specification<Restaurant> withSimiliarName(String name) {
        return (((root, query, builder) ->
                builder.like(root.get("name"), "%" + name + "%")));
    }
}
