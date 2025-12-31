package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.ProductFileControllerV2;
import com.gtech.food_api.api.V2.dto.PhotoProductDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.domain.model.PhotoProduct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import com.gtech.food_api.core.security.UsersJwtSecurity;
@Component
public class PhotoProductDTOAssemblerV2 extends RepresentationModelAssemblerSupport<PhotoProduct, PhotoProductDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    public PhotoProductDTOAssemblerV2() {
        super(ProductFileControllerV2.class, PhotoProductDTO.class);
    }

    @Override
    public PhotoProductDTO toModel(PhotoProduct photoProduct) {
        PhotoProductDTO photoDTO = modelMapper.map(photoProduct, PhotoProductDTO.class);

        if (usersJwtSecurity.canViewRestaurants()) {
            photoDTO.add(linksBuilder.linkToPhotoProduct(photoProduct.getProduct().getId(), photoProduct.getProduct().getRestaurant().getId()));

            photoDTO.add(linksBuilder.linkToProduct(photoProduct.getProduct().getId(), photoProduct.getProduct().getRestaurant().getId(), "product"));
        }

        return photoDTO;
    }
}
