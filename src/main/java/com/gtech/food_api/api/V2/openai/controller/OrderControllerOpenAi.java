package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.OrderInput;
import com.gtech.food_api.api.V2.dto.OrderDTO;
import com.gtech.food_api.api.V2.dto.OrderSummaryDTO;
import com.gtech.food_api.domain.filter.OrderFilter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Orders")
public interface OrderControllerOpenAi {
    
    @Operation(summary = "List orders with filters", description = "Retrieves a paginated list of orders with optional filters. Supports filtering by client ID, restaurant ID, and date range. Supports pagination and sorting. Example: Get orders from restaurant ID 1 between 2025-01-01 and 2025-01-31, page 0, size 10.")
    ResponseEntity<PagedModel<OrderSummaryDTO>> listAll(
        @Parameter(description = "Filter parameters: clientId, restaurantId, creationDateStart, creationDateEnd", required = false) OrderFilter filter,
        @Parameter(description = "Pagination parameters (page, size, sort). Default: page=0, size=10", required = false, example = "page=0&size=10") Pageable pageable
    );

    @Operation(summary = "Find order by code", description = "Retrieves complete detailed information about a specific order by its unique code. Example: Get order with code 'abc-123-def' to see all order details including items, client, restaurant, and status.")
    ResponseEntity<OrderDTO> findById(
        @Parameter(description = "Unique order code (UUID format)", required = true, example = "abc-123-def-456") String orderCode
    );

    @Operation(summary = "Create a new order", description = "Creates a new order. The client ID is automatically assigned from the authenticated user's token. Requires restaurant, payment method, delivery address, and items. Example: Create order with restaurant ID 1, payment method ID 2, delivery address, and items (2x Pizza, 1x Burger).")
    ResponseEntity<OrderDTO> save(
        @RequestBody(description = "Order data containing restaurant, payment method, delivery address, and items list", required = true) OrderInput orderInput
    );
    
}

