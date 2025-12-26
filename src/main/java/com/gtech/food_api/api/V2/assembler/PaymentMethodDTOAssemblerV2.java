package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.PaymentMethodControllerV2;
import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.domain.model.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter PaymentMethod em PaymentMethodDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade PaymentMethod em um DTO PaymentMethodDTO
 */
@Component
public class PaymentMethodDTOAssemblerV2 extends RepresentationModelAssemblerSupport<PaymentMethod, PaymentMethodDTO> {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;
    public PaymentMethodDTOAssemblerV2() {
        super(PaymentMethodControllerV2.class, PaymentMethodDTO.class);
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
}
