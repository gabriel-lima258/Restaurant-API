package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.StateInput;
import com.gtech.food_api.api.V2.dto.StateDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface StateControllerOpenAi {
    
    ResponseEntity<CollectionModel<StateDTO>> listAll();

    ResponseEntity<StateDTO> findById(Long id);

    ResponseEntity<StateDTO> save(StateInput stateInput);

    ResponseEntity<StateDTO> update(Long id, StateInput stateInput);

    ResponseEntity<Void> delete(Long id);
    
}

