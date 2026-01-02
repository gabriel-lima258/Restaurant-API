package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.CityInput;
import com.gtech.food_api.api.V2.dto.CityDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface CityControllerOpenAi {
    
    ResponseEntity<CollectionModel<CityDTO>> listAll();

    ResponseEntity<CityDTO> findById(Long id);

    ResponseEntity<CityDTO> save(CityInput cityInput);

    ResponseEntity<CityDTO> update(Long id, CityInput cityInput);

    ResponseEntity<Void> delete(Long id);
    
}
