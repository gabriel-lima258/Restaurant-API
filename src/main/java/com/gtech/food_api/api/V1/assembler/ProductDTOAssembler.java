package com.gtech.food_api.api.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.controller.ProductController;
import com.gtech.food_api.api.dto.ProductDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDTOAssembler extends RepresentationModelAssemblerSupport<Product, ProductDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public ProductDTOAssembler() {
        super(ProductController.class, ProductDTO.class);
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


    // @Override
    // public CollectionModel<ProductDTO> toCollectionModel(Iterable<? extends Product> entities) {
    //     return super.toCollectionModel(entities)
    //         .add(linksBuilder.linkToProducts(entities.));
    // }

    // public ProductDTO copyToDTO(Product product) {
    //     return modelMapper.map(product, ProductDTO.class);
    // }

    // public List<ProductDTO> toCollectionDTO(Collection<Product> products) {
    //     return products.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
