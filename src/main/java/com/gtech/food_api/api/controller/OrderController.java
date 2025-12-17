package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.OrderDTOAssembler;
import com.gtech.food_api.api.assembler.OrderSummaryDTOAssembler;
import com.gtech.food_api.api.disassembler.OrderInputDisassembler;
import com.gtech.food_api.api.dto.OrderDTO;
import com.gtech.food_api.api.dto.OrderSummaryDTO;
import com.gtech.food_api.api.dto.input.OrderInput;
import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.OrderService;
import com.gtech.food_api.domain.service.SubmitOrderService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubmitOrderService submitOrderService;

    @Autowired
    private OrderDTOAssembler orderDTOAssembler;

    @Autowired
    private OrderSummaryDTOAssembler orderSummaryDTOAssembler;

    @Autowired
    private OrderInputDisassembler orderInputDisassembler;

    @GetMapping
    public ResponseEntity<List<OrderSummaryDTO>> listAll(){
        List<Order> result = orderService.listAll();
        List<OrderSummaryDTO> dtoList = orderSummaryDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
        Order entity = orderService.findOrFail(id);
        OrderDTO dto = orderDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> save(@RequestBody @Valid OrderInput orderInput) {
        try {
            Order order = orderInputDisassembler.copyToEntity(orderInput);

            // get user authenticated
            order.setClient(new User());
            order.getClient().setId(1L);

            Order newOrder = submitOrderService.submitOrder(order);
            OrderDTO dto = orderDTOAssembler.copyToDTO(newOrder);
            
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(newOrder.getId()).toUri();

            return ResponseEntity.created(uri).body(dto);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }
}
