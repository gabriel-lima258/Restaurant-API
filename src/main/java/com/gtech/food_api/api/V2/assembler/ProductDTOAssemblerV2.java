package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.ProductControllerV2;
import com.gtech.food_api.api.V2.dto.ProductDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.domain.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Product, ProductDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    public ProductDTOAssemblerV2() {
        super(ProductControllerV2.class, ProductDTO.class);
    }

    @Override
    public ProductDTO toModel(Product product) {
        ProductDTO productDTO = createModelWithId(
            product.getId(), product, product.getRestaurant().getId());
        modelMapper.map(product, productDTO);

        productDTO.add(linksBuilder.linkToProducts(product.getRestaurant().getId(), "products"));

        productDTO.add(linksBuilder.linkToPhotoProduct(product.getId(), product.getRestaurant().getId(), "photo"));

        return productDTO;
    }
}
