package com.gtech.food_api.api.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.model.input.KitchenInput;
import com.gtech.food_api.domain.model.Kitchen;

/**
 * Disassembler para converter KitchenInput em Kitchen
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um KitchenInput em uma entidade Kitchen
 */
@Component
public class KitchenInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;

    public Kitchen copyToDomainObject(KitchenInput kitchenInput) {
        return modelMapper.map(kitchenInput, Kitchen.class);
    }
}
