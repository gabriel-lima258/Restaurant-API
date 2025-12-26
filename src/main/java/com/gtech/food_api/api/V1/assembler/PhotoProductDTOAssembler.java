package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.controller.ProductFileController;
import com.gtech.food_api.api.dto.PhotoProductDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.PhotoProduct;

@Component
public class PhotoProductDTOAssembler extends RepresentationModelAssemblerSupport<PhotoProduct, PhotoProductDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public PhotoProductDTOAssembler() {
        super(ProductFileController.class, PhotoProductDTO.class);
    }

    @Override
    public PhotoProductDTO toModel(PhotoProduct photoProduct) {
        PhotoProductDTO photoDTO = modelMapper.map(photoProduct, PhotoProductDTO.class);

        photoDTO.add(linksBuilder.linkToPhotoProduct(photoProduct.getProduct().getId(), photoProduct.getProduct().getRestaurant().getId()));

        photoDTO.add(linksBuilder.linkToProduct(photoProduct.getProduct().getId(), photoProduct.getProduct().getRestaurant().getId(), "product"));

        return photoDTO;
    }

    // public PhotoProductDTO copyToDTO(PhotoProduct photoProduct) {
    //     // Kitchen -> KitchenDTO
    //     return modelMapper.map(photoProduct, PhotoProductDTO.class);
    // }
}
