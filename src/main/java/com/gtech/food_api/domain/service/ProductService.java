package com.gtech.food_api.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.repository.ProductRepository;
import com.gtech.food_api.domain.service.exceptions.ProductNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> listAll(Restaurant restaurant, Boolean active){
        return productRepository.findByRestaurant(restaurant, active);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }   

    @Transactional(readOnly = true)
    public Product findOrFail(Long productId, Long restaurantId) {
        return productRepository.findById(productId, restaurantId).orElseThrow(()
                -> new ProductNotFoundException(productId, restaurantId));
    }
}
