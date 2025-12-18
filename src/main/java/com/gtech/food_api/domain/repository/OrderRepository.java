package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 * JpaSpecificationExecutor: permite passar filtros para dentro de metodos, exemplo: findAll(withFilter)
 * OrderRepositoryQueries: repositorio de orders queries
 * CustomJpaRepository: repositorio customizado para criar metodos genericos nossos no jpa e usar em outros repositorios
 */
@Repository
public interface OrderRepository extends CustomJpaRepository<Order, Long>, OrderRepositoryQueries, JpaSpecificationExecutor<Order> {

    @Query("FROM Order o JOIN FETCH o.client JOIN FETCH o.restaurant r JOIN FETCH r.kitchen")
    List<Order> findAll();
}
