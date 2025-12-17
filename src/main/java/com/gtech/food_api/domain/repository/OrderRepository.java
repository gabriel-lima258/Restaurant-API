package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CustomJpaRepository<Order, Long> {

    @Query("FROM Order o JOIN FETCH o.client JOIN FETCH o.restaurant r JOIN FETCH r.kitchen")
    List<Order> findAll();
}
