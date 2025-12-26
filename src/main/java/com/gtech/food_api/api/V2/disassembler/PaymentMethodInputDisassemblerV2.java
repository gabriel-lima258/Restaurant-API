package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.PaymentMethodInput;
import com.gtech.food_api.domain.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Disassembler para converter KitchenInput em Kitchen
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToDomainObject: converte um KitchenInput em uma entidade Kitchen
 */
@Component
public class PaymentMethodInputDisassemblerV2 {
    
    @Autowired
    private ModelMapper modelMapper;

    public PaymentMethod copyToEntity(PaymentMethodInput paymentMethodInput) {
        return modelMapper.map(paymentMethodInput, PaymentMethod.class);
    }

    public void copyToDomainObject(PaymentMethodInput paymentMethodInput, PaymentMethod paymentMethod) {
        modelMapper.map(paymentMethodInput, paymentMethod);
    }
}
