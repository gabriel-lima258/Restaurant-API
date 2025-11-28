package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenRepository extends JpaRepository<Kitchen, Long> {

}
