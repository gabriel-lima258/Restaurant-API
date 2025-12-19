package com.gtech.food_api.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.gtech.food_api.core.validation.FileContentType;
import com.gtech.food_api.core.validation.FileSize;

@Getter
@Setter
public class ProductFileInput {
    
    @NotNull
    @FileSize(max = "1MB")
    @FileContentType(allowed = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    private MultipartFile file;
    @NotBlank
    private String description;
    
}
