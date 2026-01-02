package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.ProductInput;
import com.gtech.food_api.api.V2.dto.ProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Products")
public interface ProductControllerOpenAi {
    
    @Operation(summary = "List products by restaurant", description = "Retrieves all products from a specific restaurant. The 'active' parameter is optional - if not provided, returns all products. Example: Get all active products from restaurant ID 1, or all products (active and inactive) if active parameter is omitted.")
    ResponseEntity<CollectionModel<ProductDTO>> listAll(Long restaurantId, Boolean active);

    @Operation(summary = "Find product by ID", description = "Retrieves detailed information about a specific product by its ID and restaurant ID. Example: Get product ID 5 from restaurant ID 1 to see 'Pizza Margherita' details with price and description.")
    ResponseEntity<ProductDTO> findById(Long productId, Long restaurantId);

    @Operation(summary = "Create a new product", description = "Creates a new product (menu item) for a specific restaurant. Requires name, description, price, and active status. Example: Create 'Burger' product for restaurant ID 1 with price R$ 25.00.")
    ResponseEntity<ProductDTO> save(Long restaurantId, ProductInput productInput);

    @Operation(summary = "Update product", description = "Updates product information including name, description, price, and active status. Example: Update product ID 3 from restaurant ID 1 to change price from R$ 30.00 to R$ 35.00.")
    ResponseEntity<ProductDTO> update(Long productId, Long restaurantId, ProductInput productInput);
    
}

