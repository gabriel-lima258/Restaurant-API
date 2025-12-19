package com.gtech.food_api.api.dto.input;

import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductFileInput {
    
    private MultipartFile file;
    private String description;
}
