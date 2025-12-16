package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.PermissionDTOAssembler;
import com.gtech.food_api.api.dto.PermissionDTO;
import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.service.GroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/groups/{groupId}/permissions")
public class GroupPermissionController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionDTOAssembler permissionDTOAssembler;

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
