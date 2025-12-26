package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.PermissionDTOAssemblerV2;
import com.gtech.food_api.api.V2.dto.PermissionDTO;
import com.gtech.food_api.domain.model.Permission;
import com.gtech.food_api.domain.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(value = "/v2/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionControllerV2 {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionDTOAssemblerV2 permissionDTOAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<PermissionDTO>> listAll(){
        List<Permission> permissions = permissionRepository.findAll();
        CollectionModel<PermissionDTO> dtoList = permissionDTOAssembler.toCollectionModel(permissions);
        return ResponseEntity.ok().body(dtoList);
    }
}
