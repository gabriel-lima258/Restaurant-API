package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.ProductInput;
import com.gtech.food_api.domain.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Disassembler para converter ProductInput em Product
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToEntity: converte um DTO ProductInput em uma entidade Product
 */
@Component
public class ProductInputDisassemblerV2 {

    @Autowired
    private ModelMapper modelMapper;

    public Product copyToEntity(ProductInput productInput) {
        // ProductInput -> Product
        return modelMapper.map(productInput, Product.class);
    }

    public void copyToDomainObject(ProductInput productInput, Product product) {
        // ProductInput -> Product em update
        modelMapper.map(productInput, product);
    }
    
}
