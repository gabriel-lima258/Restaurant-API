package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.dto.PhotoProductDTO;
import com.gtech.food_api.domain.model.PhotoProduct;

@Component
public class PhotoProductDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PhotoProductDTO copyToDTO(PhotoProduct photoProduct) {
        // Kitchen -> KitchenDTO
        return modelMapper.map(photoProduct, PhotoProductDTO.class);
    }
}
