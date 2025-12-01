package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;

// repository from Infra is best for performance and flexibility
public interface RestaurantRepositoryQueries {

    List<Restaurant> find(String name, BigDecimal minShippingFee, BigDecimal maxShippingFee);
}
