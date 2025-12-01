package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// JPASpecification permite passar filtros para dentro de metodos, exemplo: findAll(withFreeFee)
@Repository
public interface RestaurantRepository extends CustomJpaRepository<Restaurant, Long>, RestaurantRepositoryQueries,
        JpaSpecificationExecutor<Restaurant> {

    // find restaurants by shipping fee range
    List<Restaurant> findByShippingFeeBetween(BigDecimal min, BigDecimal max);

    // search by name is implemented on META-INF/orm.xml
    List<Restaurant> searchByName(String name);

    // find first restaurant by name
    Optional<Restaurant> findFirstByNameContaining(String name);

    // find 2 first restaurants by name
    List<Restaurant> findTop2ByNameContaining(Long id);

    // count restaurants by kitchen
    int countByKitchenId(Long cozinhaId);
}
