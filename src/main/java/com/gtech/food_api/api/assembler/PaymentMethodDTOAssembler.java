package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.controller.PaymentMethodController;
import com.gtech.food_api.api.dto.PaymentMethodDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
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
public class PaymentMethodDTOAssembler extends RepresentationModelAssemblerSupport<PaymentMethod, PaymentMethodDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public PaymentMethodDTOAssembler() {
        super(PaymentMethodController.class, PaymentMethodDTO.class);
    }

    @Override
    public PaymentMethodDTO toModel(PaymentMethod paymentMethod) {
        PaymentMethodDTO paymentMethodDTO = createModelWithId(paymentMethod.getId(), paymentMethod);
        modelMapper.map(paymentMethod, paymentMethodDTO);
        return paymentMethodDTO;
    }


    public PaymentMethodDTO toModelWithSelf(PaymentMethod paymentMethod) {
        PaymentMethodDTO paymentMethodDTO = toModel(paymentMethod);

        paymentMethodDTO.add(linksBuilder.linkToPaymentMethods());
 
        return paymentMethodDTO;
    }


    @Override
    public CollectionModel<PaymentMethodDTO> toCollectionModel(Iterable<? extends PaymentMethod> entities) {
        return super.toCollectionModel(entities)
            .add(linksBuilder.linkToPaymentMethods());
    }

    // public PaymentMethodDTO copyToDTO(PaymentMethod paymentMethod) {
    //     // Kitchen -> KitchenDTO
    //     return modelMapper.map(paymentMethod, PaymentMethodDTO.class);
    // }

    // public List<PaymentMethodDTO> toCollectionDTO(Collection<PaymentMethod> paymentMethods) {
    //     return paymentMethods.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
