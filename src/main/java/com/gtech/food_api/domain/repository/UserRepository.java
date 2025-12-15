package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CustomJpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
}
