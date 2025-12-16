package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CustomJpaRepository<Product, Long> {

    @Query("FROM Product p WHERE p.id = :productId AND p.restaurant.id = :restaurantId")
    Optional<Product> findById(@Param("productId") Long productId, @Param("restaurantId") Long restaurantId);
    
    List<Product> findByRestaurant(Restaurant restaurant);
}
