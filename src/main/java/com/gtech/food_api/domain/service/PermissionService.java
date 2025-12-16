package com.gtech.food_api.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.Permission;
import com.gtech.food_api.domain.repository.PermissionRepository;
import com.gtech.food_api.domain.service.exceptions.PermissionNotFoundException;

@Service
public class PermissionService {
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Transactional(readOnly = true)
    public Permission findOrFail(Long permissionId) {
        return permissionRepository.findById(permissionId).orElseThrow(
            () -> new PermissionNotFoundException(permissionId));
    }
}
