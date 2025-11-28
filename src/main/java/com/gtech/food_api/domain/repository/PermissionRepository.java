package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
