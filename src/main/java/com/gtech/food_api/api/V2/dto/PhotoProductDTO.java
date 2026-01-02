package com.gtech.food_api.api.V2.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Schema(name = "PhotoProduct", description = "Product photo representation")
@Getter
@Setter 
public class PhotoProductDTO extends RepresentationModel<PhotoProductDTO> {

    @Schema(description = "File name", example = "pizza-margherita.jpg")
    private String fileName;
    @Schema(description = "Photo description", example = "Delicious pizza photo")
    private String description;
    @Schema(description = "Content type", example = "image/jpeg")
    private String contentType;
    @Schema(description = "File size in bytes", example = "102400")
    private Long size;
}
