package com.gtech.food_api.api.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.dto.input.KitchenInput;
import com.gtech.food_api.api.dto.input.OrderInput;
import com.gtech.food_api.domain.model.Order;

/**
 * Disassembler para converter OrderInput em Order
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um OrderInput em uma entidade Order
 */
@Component
public class OrderInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;

    public Order copyToEntity(OrderInput orderInput) {
        return modelMapper.map(orderInput, Order.class);
    }

    public void copyToDomainObject(OrderInput orderInput, Order order) {
        modelMapper.map(orderInput, order);
    }
}
