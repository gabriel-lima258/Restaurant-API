package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.OrderInput;
import com.gtech.food_api.api.V2.dto.OrderDTO;
import com.gtech.food_api.api.V2.dto.OrderSummaryDTO;
import com.gtech.food_api.domain.filter.OrderFilter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface OrderControllerOpenAi {
    
    ResponseEntity<PagedModel<OrderSummaryDTO>> listAll(OrderFilter filter, Pageable pageable);

    ResponseEntity<OrderDTO> findById(String orderCode);

    ResponseEntity<OrderDTO> save(OrderInput orderInput);
    
}

