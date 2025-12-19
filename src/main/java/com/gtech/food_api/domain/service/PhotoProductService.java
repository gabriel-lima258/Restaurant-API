package com.gtech.food_api.domain.service;

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
        return productRepository.savePhoto(photoProduct);
    }  
}
