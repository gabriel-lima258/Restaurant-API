package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Kitchen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KitchenRepository extends CustomJpaRepository<Kitchen, Long> {

    List<Kitchen> findByNameContaining(String name);

    Optional<Kitchen> findByName(String name);

    // custom query method, returns true if a kitchen with the given name exists
    boolean existsByName(String name);
}
