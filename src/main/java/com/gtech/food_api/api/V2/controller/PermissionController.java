package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.PermissionDTOAssembler;
import com.gtech.food_api.api.V1.dto.PermissionDTO;
import com.gtech.food_api.domain.model.Permission;
import com.gtech.food_api.domain.repository.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/v1/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionDTOAssembler permissionDTOAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<PermissionDTO>> listAll(){
        List<Permission> permissions = permissionRepository.findAll();
        CollectionModel<PermissionDTO> dtoList = permissionDTOAssembler.toCollectionModel(permissions);
        return ResponseEntity.ok().body(dtoList);
    }
}
