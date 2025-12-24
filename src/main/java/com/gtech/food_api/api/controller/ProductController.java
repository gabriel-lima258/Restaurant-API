package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.ProductDTOAssembler;
import com.gtech.food_api.api.disassembler.ProductInputDisassembler;
import com.gtech.food_api.api.dto.ProductDTO;
import com.gtech.food_api.api.dto.input.ProductInput;
import com.gtech.food_api.api.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.ProductService;
import com.gtech.food_api.domain.service.RestaurantService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller para gerenciar os métodos de pagamento de um restaurante
 * 
 * Este endpoint é responsável por gerenciar os métodos de pagamento de um restaurante
 * Ele mapeia Many to Many entre Produ e PaymentMethod
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ProductDTOAssembler productDTOAssembler;

    @Autowired 
    private ProductInputDisassembler productInputDisassembler;

    // active é opcional, se nao for informado, retorna todos os produtos
    @GetMapping
    public ResponseEntity<List<ProductDTO>> listAll(@PathVariable Long restaurantId,
            @RequestParam(required = false) Boolean active){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        List<Product> products = productService.listAll(restaurant, active);
        List<ProductDTO> dtoList = productDTOAssembler.toCollectionDTO(products);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long productId, @PathVariable Long restaurantId) {
        Product product = productService.findOrFail(productId, restaurantId);
        ProductDTO productDTO = productDTOAssembler.copyToDTO(product);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@PathVariable Long restaurantId, @RequestBody @Valid ProductInput productInput) {
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        
        // ProductInput -> Product
        Product product = productInputDisassembler.copyToEntity(productInput);
        product.setRestaurant(restaurant); 
        Product entity = productService.save(product);
        // Product -> ProductDTO
        ProductDTO dto = productDTOAssembler.copyToDTO(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long productId, @PathVariable Long restaurantId, @RequestBody ProductInput productInput) {
        Product product = productService.findOrFail(productId, restaurantId);
        // Copy ProductInput values to Product entity
        productInputDisassembler.copyToDomainObject(productInput, product);
        productService.save(product);
        ProductDTO dto = productDTOAssembler.copyToDTO(product);
        return ResponseEntity.ok().body(dto);
    }
}
