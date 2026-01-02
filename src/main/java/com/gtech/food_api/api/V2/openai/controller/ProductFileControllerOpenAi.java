package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.gtech.food_api.api.V2.dto.input.ProductFileInput;
import com.gtech.food_api.api.V2.dto.PhotoProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Product Photos")
public interface ProductFileControllerOpenAi {
    
    @Operation(summary = "Get product photo metadata", description = "Retrieves metadata information about a product's photo in JSON format. Example: Get photo information for product ID 5 from restaurant ID 1 to see file name, size, and content type.",
    responses = {
        @ApiResponse(responseCode = "200", 
        description = "Photo metadata retrieved successfully",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(ref = "PhotoProductDTO")),
            @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
            @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
        }),
        @ApiResponse(responseCode = "404", description = "Product, restaurant, or photo not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<PhotoProductDTO> getPhoto(
        @Parameter(description = "ID of the product", required = true, example = "5") Long productId,
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );

    @Operation(hidden = true)
    ResponseEntity<InputStreamResource> downloadPhoto(
        @Parameter(description = "ID of the product", required = true, example = "5") Long productId,
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @Parameter(description = "Accept header specifying desired image format (image/jpeg, image/png, etc.)", required = true, example = "image/jpeg") String acceptHeader
    ) throws HttpMediaTypeNotAcceptableException;

    @Operation(summary = "Upload product photo", description = "Uploads a photo for a product. Accepts JPEG or PNG images with maximum size of 1MB. Requires the image file and a description. Example: Upload a JPEG photo (max 1MB) for product ID 5 from restaurant ID 1 with description 'Delicious pizza photo'.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Photo uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file format, size exceeds 1MB, or validation errors", content = @Content(schema = @Schema(ref = "Exceptions"))),
        @ApiResponse(responseCode = "404", description = "Product or restaurant not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<PhotoProductDTO> uploadPhoto(
        @Parameter(description = "ID of the product", required = true, example = "5") Long productId,
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @RequestBody(description = "Photo data containing image file (JPEG/PNG, max 1MB) and description", required = true) ProductFileInput input
    ) throws IOException;

    @Operation(summary = "Delete product photo", description = "Removes the photo associated with a product. Example: Delete photo for product ID 5 from restaurant ID 1.",
    responses = {
        @ApiResponse(responseCode = "204", description = "Photo deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product, restaurant, or photo not found", content = @Content(schema = @Schema(ref = "Exceptions")))
    })
    ResponseEntity<PhotoProductDTO> deletePhoto(
        @Parameter(description = "ID of the product", required = true, example = "5") Long productId,
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );
    
}

