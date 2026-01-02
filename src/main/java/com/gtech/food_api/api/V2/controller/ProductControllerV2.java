package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.ProductDTOAssemblerV2;
import com.gtech.food_api.api.V2.disassembler.ProductInputDisassemblerV2;
import com.gtech.food_api.api.V2.dto.ProductDTO;
import com.gtech.food_api.api.V2.dto.input.ProductInput;
import com.gtech.food_api.api.V2.openai.controller.ProductControllerOpenAi;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.api.V2.utils.ResourceUriHelper;
import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.ProductService;
import com.gtech.food_api.domain.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * Controller para gerenciar os métodos de pagamento de um restaurante
 * 
 * Este endpoint é responsável por gerenciar os métodos de pagamento de um restaurante
 * Ele mapeia Many to Many entre Produ e PaymentMethod
 */
@RestController
@RequestMapping(value = "/v2/restaurants/{restaurantId}/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductControllerV2 implements ProductControllerOpenAi {

    @Autowired
    private ProductService productService;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ProductDTOAssemblerV2 productDTOAssembler;

    @Autowired 
    private ProductInputDisassemblerV2 productInputDisassembler;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    // active é opcional, se nao for informado, retorna todos os produtos
    @CheckSecurity.Restaurants.CanView
    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<ProductDTO>> listAll(@PathVariable Long restaurantId,
                                                               @RequestParam(required = false) Boolean active){
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        List<Product> products = productService.listAll(restaurant, active);
        CollectionModel<ProductDTO> dtoList = productDTOAssembler.toCollectionModel(products)
        .add(linksBuilder.linkToProducts(restaurantId, "products"));

        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.Restaurants.CanView
    @Override
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long productId, @PathVariable Long restaurantId) {
        Product product = productService.findOrFail(productId, restaurantId);
        ProductDTO productDTO = productDTOAssembler.toModel(product);
        return ResponseEntity.ok().body(productDTO);
    }

    @CheckSecurity.Restaurants.CanOnwerManager
    @Override
    @PostMapping
    public ResponseEntity<ProductDTO> save(@PathVariable Long restaurantId, @RequestBody @Valid ProductInput productInput) {
        Restaurant restaurant = restaurantService.findOrFail(restaurantId);
        
        // ProductInput -> Product
        Product product = productInputDisassembler.copyToEntity(productInput);
        product.setRestaurant(restaurant); 
        Product entity = productService.save(product);
        // Product -> ProductDTO
        ProductDTO dto = productDTOAssembler.toModel(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());

        return ResponseEntity.created(uri).body(dto);
    }

    @CheckSecurity.Restaurants.CanOnwerManager
    @Override
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long productId, @PathVariable Long restaurantId, @RequestBody ProductInput productInput) {
        Product product = productService.findOrFail(productId, restaurantId);
        // Copy ProductInput values to Product entity
        productInputDisassembler.copyToDomainObject(productInput, product);
        productService.save(product);
        ProductDTO dto = productDTOAssembler.toModel(product);
        return ResponseEntity.ok().body(dto);
    }
}
