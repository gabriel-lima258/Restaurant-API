package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.PhotoProductDTOAssembler;
import com.gtech.food_api.api.assembler.ProductDTOAssembler;
import com.gtech.food_api.api.disassembler.ProductInputDisassembler;
import com.gtech.food_api.api.dto.PhotoProductDTO;
import com.gtech.food_api.api.dto.ProductDTO;
import com.gtech.food_api.api.dto.input.ProductFileInput;
import com.gtech.food_api.api.dto.input.ProductInput;
import com.gtech.food_api.domain.model.PhotoProduct;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.PhotoProductService;
import com.gtech.food_api.domain.service.PhotoStorageService;
import com.gtech.food_api.domain.service.ProductService;
import com.gtech.food_api.domain.service.RestaurantService;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;
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
    private PhotoProductService photoProductService;

    @Autowired
    private PhotoStorageService photoStorageService;

    @Autowired
    private ProductDTOAssembler productDTOAssembler;

    @Autowired
    private PhotoProductDTOAssembler photoProductDTOAssembler;

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
        ProductDTO productDTO = productDTOAssembler.copyToDTO(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(entity.getId()).toUri();

        return ResponseEntity.created(uri).body(productDTO);
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

    
    @PutMapping(value = "/{productId}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PhotoProductDTO> uploadPhoto(@PathVariable Long productId, @PathVariable Long restaurantId, @Valid ProductFileInput input) throws IOException {

        Product product = productService.findOrFail(productId, restaurantId);

        // extrai o arquivo do multipart file dentro input
        MultipartFile file = input.getFile();

        PhotoProduct photoProduct = new PhotoProduct();
        photoProduct.setProduct(product);
        photoProduct.setFileName(file.getOriginalFilename());
        photoProduct.setDescription(input.getDescription());
        photoProduct.setContentType(file.getContentType());
        photoProduct.setSize(file.getSize());

        photoProductService.savePhoto(photoProduct, file.getInputStream());

        PhotoProductDTO photoDTO = photoProductDTOAssembler.copyToDTO(photoProduct);

        return ResponseEntity.ok().body(photoDTO);
    }

    @GetMapping(value = "{productId}/photo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoProductDTO> getPhoto(@PathVariable Long productId, @PathVariable Long restaurantId) {
        PhotoProduct photoProduct = photoProductService.findOrFail(productId, restaurantId);
        PhotoProductDTO photoDTO = photoProductDTOAssembler.copyToDTO(photoProduct);
        return ResponseEntity.ok().body(photoDTO);
    }
    
    /*
    * Request header é o header da requisição, ex: accept: image/jpeg, image/png, image/gif
    * throws HttpMediaTypeNotAcceptableException é uma exceção que é lançada quando o tipo de arquivo não é compatível com o tipo requerido pelo cliente
    */
    @GetMapping(value = "{productId}/photo")
    public ResponseEntity<InputStreamResource> downloadPhoto(@PathVariable Long productId, @PathVariable Long restaurantId,
        @RequestHeader("accept") String acceptHeader
    ) throws HttpMediaTypeNotAcceptableException {
        try {
            PhotoProduct photoProduct = photoProductService.findOrFail(productId, restaurantId);

            // parseia String acceptHeader para um objeto MediaType
            MediaType mediaType = MediaType.parseMediaType(photoProduct.getContentType());
            List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);

            // verifica se o tipo de arquivo real é compatível com o tipo requerido pelo cliente
            verifyTypeFile(mediaType, acceptedMediaTypes);

            InputStream file = photoStorageService.recoverFile(photoProduct.getFileName());
            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(new InputStreamResource(file));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private void verifyTypeFile(MediaType type, List<MediaType> acceptedMediaTypes) throws HttpMediaTypeNotAcceptableException {
        boolean isCompatible = acceptedMediaTypes.stream()
            .anyMatch(mediaType -> mediaType.isCompatibleWith(type));

        if (!isCompatible) {
            throw new HttpMediaTypeNotAcceptableException(acceptedMediaTypes);
        }
    }
}
