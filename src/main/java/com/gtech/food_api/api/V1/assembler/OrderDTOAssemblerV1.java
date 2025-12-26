package com.gtech.food_api.api.V1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gtech.food_api.api.V1.dto.OrderDTO;
import com.gtech.food_api.domain.model.Order;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter Kitchen em KitchenDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Order em um DTO OrderDTO
 */
@Component
public class OrderDTOAssemblerV1 {
    @Autowired
    private ModelMapper modelMapper;

    public OrderDTO copyToDTO(Order order) {
        // Order -> OrderDTO
        return modelMapper.map(order, OrderDTO.class);
    }
    public List<OrderDTO> toCollectionDTO(Collection<Order> orders) {
        return orders.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
