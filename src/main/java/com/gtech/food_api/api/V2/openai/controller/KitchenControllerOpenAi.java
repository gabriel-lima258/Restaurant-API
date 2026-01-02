package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.KitchenInput;
import com.gtech.food_api.api.V2.dto.KitchenDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface KitchenControllerOpenAi {
    
    ResponseEntity<PagedModel<KitchenDTO>> listAll(Pageable pageable);

    ResponseEntity<KitchenDTO> findById(Long id);

    ResponseEntity<KitchenDTO> save(KitchenInput kitchenInput);

    ResponseEntity<KitchenDTO> update(Long id, KitchenInput kitchenInput);

    ResponseEntity<Void> delete(Long id);
    
}

