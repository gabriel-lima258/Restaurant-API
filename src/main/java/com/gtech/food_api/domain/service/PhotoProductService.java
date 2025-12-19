package com.gtech.food_api.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.PhotoProduct;
import com.gtech.food_api.domain.repository.ProductRepository;

@Service
public class PhotoProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public PhotoProduct savePhoto(PhotoProduct photoProduct) {
        Long productId = photoProduct.getProductId();
        Long restaurantId = photoProduct.getRestaurantId();

        Optional<PhotoProduct> photoProductOptional = productRepository.findPhotoById(productId, restaurantId);

        if (photoProductOptional.isPresent()) {
            // exclui a foto existente
            productRepository.deletePhoto(photoProductOptional.get());
            productRepository.flush(); // Força a execução da query no banco de dados antes de salvar a nova foto
        }

        return productRepository.savePhoto(photoProduct);
    }  
}
