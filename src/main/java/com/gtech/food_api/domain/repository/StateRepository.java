package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
}
