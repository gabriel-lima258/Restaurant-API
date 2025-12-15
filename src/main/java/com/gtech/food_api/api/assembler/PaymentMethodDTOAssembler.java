package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.dto.PaymentMethodDTO;
import com.gtech.food_api.domain.model.PaymentMethod;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter PaymentMethod em PaymentMethodDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade PaymentMethod em um DTO PaymentMethodDTO
 */
@Component
public class PaymentMethodDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PaymentMethodDTO copyToDTO(PaymentMethod paymentMethod) {
        // Kitchen -> KitchenDTO
        return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    }

    public List<PaymentMethodDTO> toCollectionDTO(Collection<PaymentMethod> paymentMethods) {
        return paymentMethods.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
