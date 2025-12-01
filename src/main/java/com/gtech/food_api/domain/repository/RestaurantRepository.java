package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // find restaurants by shipping fee range
    List<Restaurant> findByShippingFeeBetween(BigDecimal min, BigDecimal max);

    // find first restaurant by name
    Optional<Restaurant> findFirstByNameContaining(String name);

    // find 2 first restaurants by name
    List<Restaurant> findTop2ByNameContaining(Long id);

    // count restaurants by kitchen
    int countByKitchenId(Long cozinhaId);
}
