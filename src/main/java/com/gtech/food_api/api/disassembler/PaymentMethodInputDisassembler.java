package com.gtech.food_api.api.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.dto.input.KitchenInput;
import com.gtech.food_api.api.dto.input.PaymentMethodInput;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.model.Kitchen;

/**
 * Disassembler para converter KitchenInput em Kitchen
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um KitchenInput em uma entidade Kitchen
 */
@Component
public class PaymentMethodInputDisassembler {
    
    @Autowired
    private ModelMapper modelMapper;

    public PaymentMethod copyToEntity(PaymentMethodInput paymentMethodInput) {
        return modelMapper.map(paymentMethodInput, PaymentMethod.class);
    }

    public void copyToDomainObject(PaymentMethodInput paymentMethodInput, PaymentMethod paymentMethod) {
        modelMapper.map(paymentMethodInput, paymentMethod);
    }
}
