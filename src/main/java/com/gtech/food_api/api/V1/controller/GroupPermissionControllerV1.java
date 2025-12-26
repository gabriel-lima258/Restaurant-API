package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.PermissionDTOAssemblerV1;
import com.gtech.food_api.api.V1.dto.PermissionDTO;
import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.MediaType;

@RestController
@RequestMapping(value = "/v1/groups/{groupId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupPermissionControllerV1 {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionDTOAssemblerV1 permissionDTOAssembler;


    @GetMapping
    public ResponseEntity<List<PermissionDTO>> listAll(@PathVariable Long groupId){
        Group group = groupService.findOrFail(groupId);
        List<PermissionDTO> dtoList = permissionDTOAssembler.toCollectionDTO(group.getPermissions());
        
        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<Void> addPermission(@PathVariable Long groupId, @PathVariable Long permissionId) {
        groupService.addPermission(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> removePermission(@PathVariable Long groupId, @PathVariable Long permissionId) {
        groupService.removePermission(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }
}
