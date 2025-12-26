package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.OrderInput;
import com.gtech.food_api.domain.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Disassembler para converter OrderInput em Order
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um OrderInput em uma entidade Order
 */
@Component
public class OrderInputDisassemblerV2 {
    
    @Autowired
    private ModelMapper modelMapper;

    public Order copyToEntity(OrderInput orderInput) {
        return modelMapper.map(orderInput, Order.class);
    }

    public void copyToDomainObject(OrderInput orderInput, Order order) {
        modelMapper.map(orderInput, order);
    }
}
