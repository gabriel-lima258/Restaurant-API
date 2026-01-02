package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.ProductInput;
import com.gtech.food_api.api.V2.dto.ProductDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Products")
public interface ProductControllerOpenAi {
    
    @Operation(summary = "List products by restaurant", description = "Retrieves all products from a specific restaurant. The 'active' parameter is optional - if not provided, returns all products. Example: Get all active products from restaurant ID 1, or all products (active and inactive) if active parameter is omitted.")
    ResponseEntity<CollectionModel<ProductDTO>> listAll(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @Parameter(description = "Filter by active status (true = only active, false = only inactive, null = all)", required = false, example = "true") Boolean active
    );

    @Operation(summary = "Find product by ID", description = "Retrieves detailed information about a specific product by its ID and restaurant ID. Example: Get product ID 5 from restaurant ID 1 to see 'Pizza Margherita' details with price and description.")
    ResponseEntity<ProductDTO> findById(
        @Parameter(description = "ID of the product", required = true, example = "5") Long productId,
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId
    );

    @Operation(summary = "Create a new product", description = "Creates a new product (menu item) for a specific restaurant. Requires name, description, price, and active status. Example: Create 'Burger' product for restaurant ID 1 with price R$ 25.00.")
    ResponseEntity<ProductDTO> save(
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @RequestBody(description = "Product data containing name, description, price, and active status", required = true) ProductInput productInput
    );

    @Operation(summary = "Update product", description = "Updates product information including name, description, price, and active status. Example: Update product ID 3 from restaurant ID 1 to change price from R$ 30.00 to R$ 35.00.")
    ResponseEntity<ProductDTO> update(
        @Parameter(description = "ID of the product to update", required = true, example = "3") Long productId,
        @Parameter(description = "ID of the restaurant", required = true, example = "1") Long restaurantId,
        @RequestBody(description = "Product data with updated information", required = true) ProductInput productInput
    );
    
}

