package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
