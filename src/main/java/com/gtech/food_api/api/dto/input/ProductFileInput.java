package com.gtech.food_api.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import com.gtech.food_api.core.validation.FileSize;

@Getter
@Setter
public class ProductFileInput {
    
    @NotNull
    @FileSize(max = "1MB")
    private MultipartFile file;
    @NotBlank
    private String description;
}
