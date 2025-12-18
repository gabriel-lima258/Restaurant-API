package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.filter.OrderFilter;
import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.repository.OrderRepository;
import com.gtech.food_api.domain.service.exceptions.OrderNotFoundException;
import com.gtech.food_api.infra.repository.specification.OrderSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public Page<Order> listAll(OrderFilter filter, Pageable pageable){
        // passando o filtro OrderFilter para o specification
        // OrderSpec.withFilter(filter) é um Specification<Order> que é usado para filtrar os pedidos
        return orderRepository.findAll(OrderSpec.withFilter(filter), pageable);
    }

    @Transactional(readOnly = true)
    public Order findOrFail(String orderCode) {
        return orderRepository.findByCodeWithEagerType(orderCode).orElseThrow(()
                -> new OrderNotFoundException(orderCode));
    }
}
