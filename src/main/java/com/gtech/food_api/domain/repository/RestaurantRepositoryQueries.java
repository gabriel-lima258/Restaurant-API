package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Restaurant;

import java.math.BigDecimal;
import java.util.List;

public interface RestaurantRepositoryQueries {

    List<Restaurant> find(String name, BigDecimal minShippingFee, BigDecimal maxShippingFee);

    List<Restaurant> findWithFreeFee(String name);
}
