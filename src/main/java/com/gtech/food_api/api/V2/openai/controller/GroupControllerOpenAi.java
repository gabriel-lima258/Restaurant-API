package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.GroupInput;
import com.gtech.food_api.api.V2.dto.GroupDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface GroupControllerOpenAi {
    
    ResponseEntity<CollectionModel<GroupDTO>> listAll();

    ResponseEntity<GroupDTO> findById(Long id);

    ResponseEntity<GroupDTO> save(GroupInput groupInput);

    ResponseEntity<GroupDTO> update(Long id, GroupInput groupInput);

    ResponseEntity<Void> delete(Long id);
    
}

