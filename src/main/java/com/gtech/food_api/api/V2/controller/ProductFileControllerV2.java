package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.PhotoProductDTOAssemblerV2;
import com.gtech.food_api.api.V2.dto.PhotoProductDTO;
import com.gtech.food_api.api.V2.dto.input.ProductFileInput;
import com.gtech.food_api.core.security.resource.CheckSecurity;
import com.gtech.food_api.domain.model.PhotoProduct;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.service.ProductService;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import com.gtech.food_api.domain.service.storage.PhotoProductService;
import com.gtech.food_api.domain.service.storage.PhotoStorageService;
import com.gtech.food_api.domain.service.storage.PhotoStorageService.RecoverPhoto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * Controller para gerenciar os métodos de pagamento de um restaurante
 * 
 * Este endpoint é responsável por gerenciar os métodos de pagamento de um restaurante
 * Ele mapeia Many to Many entre Produ e PaymentMethod
 */
@RestController
@RequestMapping("/v2/restaurants/{restaurantId}/products/{productId}/photo")
public class ProductFileControllerV2 {

    @Autowired
    private ProductService productService;

    @Autowired  
    private PhotoProductService photoProductService;

    @Autowired
    private PhotoStorageService photoStorageService;

    @Autowired
    private PhotoProductDTOAssemblerV2 photoProductDTOAssembler;

    @CheckSecurity.Restaurants.CanView
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PhotoProductDTO> getPhoto(@PathVariable Long productId, @PathVariable Long restaurantId) {
        PhotoProduct photoProduct = photoProductService.findOrFail(restaurantId, productId);
        PhotoProductDTO photoDTO = photoProductDTOAssembler.toModel(photoProduct);
        return ResponseEntity.ok().body(photoDTO);
    }
    
    /*
    * Request header é o header da requisição, ex: accept: image/jpeg, image/png, image/gif
    * throws HttpMediaTypeNotAcceptableException é uma exceção que é lançada quando o tipo de arquivo não é compatível com o tipo requerido pelo cliente
    * As fotos dos produtos ficarão públicas (não precisa de autorização para acessá-las)
    */
    @GetMapping(produces = MediaType.ALL_VALUE)
    public ResponseEntity<InputStreamResource> downloadPhoto(@PathVariable Long productId, @PathVariable Long restaurantId,
        @RequestHeader("accept") String acceptHeader
    ) throws HttpMediaTypeNotAcceptableException {
        try {
            PhotoProduct photoProduct = photoProductService.findOrFail(restaurantId, productId);

            // parseia String acceptHeader para um objeto MediaType
            MediaType mediaType = MediaType.parseMediaType(photoProduct.getContentType());
            List<MediaType> acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeader);

            // verifica se o tipo de arquivo real é compatível com o tipo requerido pelo cliente
            verifyTypeFile(mediaType, acceptedMediaTypes);

            RecoverPhoto file = photoStorageService.recoverFile(photoProduct.getFileName());

            // se tiver url redireciona para a url se não, retorna o arquivo
            if (file.hasUrl()) {
                return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, file.getUrl())
                    .build();
            } else {
                return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(new InputStreamResource(file.getInputStream()));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CheckSecurity.Restaurants.CanOnwerManager
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

        PhotoProductDTO photoDTO = photoProductDTOAssembler.toModel(photoProduct);

        return ResponseEntity.ok().body(photoDTO);
    }

    @CheckSecurity.Restaurants.CanOnwerManager
    @DeleteMapping
    public ResponseEntity<PhotoProductDTO> deletePhoto(@PathVariable Long productId, @PathVariable Long restaurantId) {
        try {
            photoProductService.delete(restaurantId, productId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
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
