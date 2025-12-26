package com.gtech.food_api.api.V1.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;
import com.gtech.food_api.api.V1.dto.ProductDTO;    
import com.gtech.food_api.domain.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDTOAssemblerV1 {
    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO copyToDTO(Product product) {
        return modelMapper.map(product, ProductDTO.class);
    }
    public List<ProductDTO> toCollectionDTO(Collection<Product> products) {
        return products.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
