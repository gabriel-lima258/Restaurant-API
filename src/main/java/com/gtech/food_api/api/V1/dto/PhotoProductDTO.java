package com.gtech.food_api.api.V1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter 
public class PhotoProductDTO {

    private String fileName;
    private String description;
    private String contentType;
    private Long size;
}
