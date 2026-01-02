package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.gtech.food_api.api.V2.dto.input.ProductFileInput;
import com.gtech.food_api.api.V2.dto.PhotoProductDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.io.IOException;

@SecurityRequirement(name = "security_auth")
public interface ProductFileControllerOpenAi {
    
    ResponseEntity<PhotoProductDTO> getPhoto(Long productId, Long restaurantId);

    ResponseEntity<InputStreamResource> downloadPhoto(Long productId, Long restaurantId, String acceptHeader) 
        throws HttpMediaTypeNotAcceptableException;

    ResponseEntity<PhotoProductDTO> uploadPhoto(Long productId, Long restaurantId, ProductFileInput input) 
        throws IOException;

    ResponseEntity<PhotoProductDTO> deletePhoto(Long productId, Long restaurantId);
    
}

