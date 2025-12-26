package com.gtech.food_api.api.V2.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter 
public class PhotoProductDTO extends RepresentationModel<PhotoProductDTO> {

    private String fileName;
    private String description;
    private String contentType;
    private Long size;
}
