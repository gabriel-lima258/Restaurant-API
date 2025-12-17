package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.dto.OrderSummaryDTO;
import com.gtech.food_api.domain.model.Order;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter Order em OrderSummaryDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Order em um DTO OrderDTO
 */
@Component
public class OrderSummaryDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public OrderSummaryDTO copyToDTO(Order order) {
        // Order -> OrderDTO
        return modelMapper.map(order, OrderSummaryDTO.class);
    }

    public List<OrderSummaryDTO> toCollectionDTO(Collection<Order> orders) {
        return orders.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
