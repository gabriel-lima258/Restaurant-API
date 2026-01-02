package com.gtech.food_api.api.V2.dto.input;

import com.gtech.food_api.core.validation.FileContentType;
import com.gtech.food_api.core.validation.FileSize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "ProductFileInput", description = "Product photo file input data")
@Getter
@Setter
public class ProductFileInput {
    
    @Schema(description = "Image file (JPEG or PNG, max 1MB)", example = "product-photo.jpg")
    @NotNull
    @FileSize(max = "1MB")
    @FileContentType(allowed = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    private MultipartFile file;
    @Schema(description = "Photo description", example = "Delicious pizza photo")
    @NotBlank
    private String description;
    
}
