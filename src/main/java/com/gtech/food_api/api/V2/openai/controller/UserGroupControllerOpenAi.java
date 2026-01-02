package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.GroupDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface UserGroupControllerOpenAi {
    
    ResponseEntity<CollectionModel<GroupDTO>> listAll(Long userId);

    ResponseEntity<Void> associateGroup(Long userId, Long groupId);

    ResponseEntity<Void> disassociateGroup(Long userId, Long groupId);
    
}

