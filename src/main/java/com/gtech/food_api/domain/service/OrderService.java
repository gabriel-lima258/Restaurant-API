package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.filter.OrderFilter;
import com.gtech.food_api.domain.model.Address;
import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.repository.OrderRepository;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.OrderNotFoundException;
import com.gtech.food_api.infra.repository.specification.OrderSpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<Order> listAll(OrderFilter filter){
        // passando o filtro OrderFilter para o specification
        // OrderSpec.withFilter(filter) é um Specification<Order> que é usado para filtrar os pedidos
        return orderRepository.findAll(OrderSpec.withFilter(filter));
    }

    @Transactional(readOnly = true)
    public Order findOrFail(String orderCode) {
        return orderRepository.findByCodeWithEagerType(orderCode).orElseThrow(()
                -> new OrderNotFoundException(orderCode));
    }
}
