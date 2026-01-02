package com.gtech.food_api.api.V2.openai.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import com.gtech.food_api.api.V2.dto.input.UserInput;
import com.gtech.food_api.api.V2.dto.input.UserPasswordInput;
import com.gtech.food_api.api.V2.dto.input.UserWithPasswordInput;
import com.gtech.food_api.api.V2.dto.UserDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_auth")
public interface UserControllerOpenAi {
    
    ResponseEntity<CollectionModel<UserDTO>> listAll();

    ResponseEntity<UserDTO> findById(Long id);

    ResponseEntity<UserDTO> save(UserWithPasswordInput userWithPasswordInput);

    ResponseEntity<UserDTO> update(Long id, UserInput userInput);

    ResponseEntity<Void> updatePassword(Long id, UserPasswordInput userPasswordInput);
    
}

