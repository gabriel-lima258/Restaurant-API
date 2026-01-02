package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.gtech.food_api.api.V2.dto.input.ProductFileInput;
import com.gtech.food_api.api.V2.dto.PhotoProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Product Photos")
public interface ProductFileControllerOpenAi {
    
    @Operation(summary = "Get product photo metadata", description = "Retrieves metadata information about a product's photo in JSON format. Example: Get photo information for product ID 5 from restaurant ID 1 to see file name, size, and content type.")
    ResponseEntity<PhotoProductDTO> getPhoto(Long productId, Long restaurantId);

    @Operation(summary = "Download product photo", description = "Downloads the actual product photo image. The Accept header determines the response format (image/jpeg, image/png, etc.). Example: Download photo for product ID 5 from restaurant ID 1 with Accept: image/jpeg.")
    ResponseEntity<InputStreamResource> downloadPhoto(Long productId, Long restaurantId, String acceptHeader) 
        throws HttpMediaTypeNotAcceptableException;

    @Operation(summary = "Upload product photo", description = "Uploads a photo for a product. Accepts JPEG or PNG images with maximum size of 1MB. Requires the image file and a description. Example: Upload a JPEG photo (max 1MB) for product ID 5 from restaurant ID 1 with description 'Delicious pizza photo'.")
    ResponseEntity<PhotoProductDTO> uploadPhoto(Long productId, Long restaurantId, ProductFileInput input) 
        throws IOException;

    @Operation(summary = "Delete product photo", description = "Removes the photo associated with a product. Example: Delete photo for product ID 5 from restaurant ID 1.")
    ResponseEntity<PhotoProductDTO> deletePhoto(Long productId, Long restaurantId);
    
}

