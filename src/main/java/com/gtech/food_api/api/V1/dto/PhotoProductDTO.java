package com.gtech.food_api.api.dto;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class PhotoProductDTO extends RepresentationModel<PhotoProductDTO> {

    private String fileName;
    private String description;
    private String contentType;
    private Long size;
}
