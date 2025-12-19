package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.PhotoProductDTOAssembler;
import com.gtech.food_api.api.dto.PhotoProductDTO;
import com.gtech.food_api.api.dto.input.ProductFileInput;
import com.gtech.food_api.domain.model.PhotoProduct;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.service.PhotoProductService;
import com.gtech.food_api.domain.service.PhotoStorageService;
import com.gtech.food_api.domain.service.ProductService;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


/**
 * Controller para gerenciar os métodos de pagamento de um restaurante
 * 
 * Este endpoint é responsável por gerenciar os métodos de pagamento de um restaurante
 * Ele mapeia Many to Many entre Produ e PaymentMethod
 */
@RestController
@RequestMapping("/restaurants/{restaurantId}/products/{productId}/photo")
public class ProductFileController {

    @Autowired
    private ProductService productService;

    @Autowired  
    private PhotoProductService photoProductService;

    @Autowired
    private PhotoStorageService photoStorageService;

    @Autowired
    private PhotoProductDTOAssembler photoProductDTOAssembler;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoProductDTO> getPhoto(@PathVariable Long productId, @PathVariable Long restaurantId) {
        PhotoProduct photoProduct = photoProductService.findOrFail(productId, restaurantId);
        PhotoProductDTO photoDTO = photoProductDTOAssembler.copyToDTO(photoProduct);
        return ResponseEntity.ok().body(photoDTO);
    }
    
    /*
    * Request header é o header da requisição, ex: accept: image/jpeg, image/png, image/gif
    * throws HttpMediaTypeNotAcceptableException é uma exceção que é lançada quando o tipo de arquivo não é compatível com o tipo requerido pelo cliente
    */
    @GetMapping
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

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @DeleteMapping
    public ResponseEntity<PhotoProductDTO> deletePhoto(@PathVariable Long productId, @PathVariable Long restaurantId) {
        photoProductService.delete(restaurantId, productId);
        return ResponseEntity.noContent().build();
    }

    private void verifyTypeFile(MediaType type, List<MediaType> acceptedMediaTypes) throws HttpMediaTypeNotAcceptableException {
        boolean isCompatible = acceptedMediaTypes.stream()
            .anyMatch(mediaType -> mediaType.isCompatibleWith(type));

        if (!isCompatible) {
            throw new HttpMediaTypeNotAcceptableException(acceptedMediaTypes);
        }
    }
}
