package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.ProductInput;
import com.gtech.food_api.api.V2.dto.ProductDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface ProductControllerOpenAi {
    
    ResponseEntity<CollectionModel<ProductDTO>> listAll(Long restaurantId, Boolean active);

    ResponseEntity<ProductDTO> findById(Long productId, Long restaurantId);

    ResponseEntity<ProductDTO> save(Long restaurantId, ProductInput productInput);

    ResponseEntity<ProductDTO> update(Long productId, Long restaurantId, ProductInput productInput);
    
}

