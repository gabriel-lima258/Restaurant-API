package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.PermissionDTOAssemblerV1;
import com.gtech.food_api.api.V1.dto.PermissionDTO;
import com.gtech.food_api.domain.model.Permission;
import com.gtech.food_api.domain.repository.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/v1/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissionControllerV1 {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionDTOAssemblerV1 permissionDTOAssembler;

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> listAll(){
        List<Permission> permissions = permissionRepository.findAll();
        List<PermissionDTO> dtoList = permissionDTOAssembler.toCollectionDTO(permissions);
        return ResponseEntity.ok().body(dtoList);
    }
}
