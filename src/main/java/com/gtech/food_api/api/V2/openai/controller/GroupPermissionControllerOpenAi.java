package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.PermissionDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface GroupPermissionControllerOpenAi {
    
    ResponseEntity<CollectionModel<PermissionDTO>> listAll(Long groupId);

    ResponseEntity<Void> addPermission(Long groupId, Long permissionId);

    ResponseEntity<Void> removePermission(Long groupId, Long permissionId);
    
}

