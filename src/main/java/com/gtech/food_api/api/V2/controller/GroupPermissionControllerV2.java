package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.PermissionDTOAssemblerV2;
import com.gtech.food_api.api.V2.dto.PermissionDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.resource.CheckSecurity;
import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v2/groups/{groupId}/permissions", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupPermissionControllerV2 {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionDTOAssemblerV2 permissionDTOAssembler;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @CheckSecurity.UsersGroupsPermissions.CanView
    @GetMapping
    public ResponseEntity<CollectionModel<PermissionDTO>> listAll(@PathVariable Long groupId){
        Group group = groupService.findOrFail(groupId);
        CollectionModel<PermissionDTO> dtoList = permissionDTOAssembler.toCollectionModel(group.getPermissions());
        dtoList.removeLinks()
            .add(linksBuilder.linkToGroupPermissions(groupId))
            .add(linksBuilder.linkToAssociatePermissionGroup(groupId));

        dtoList.getContent().forEach(permissionDTO -> {
            permissionDTO.add(linksBuilder.linkToRemovePermissionGroup(groupId, permissionDTO.getId()));
        });

        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PutMapping("/{permissionId}")
    public ResponseEntity<Void> addPermission(@PathVariable Long groupId, @PathVariable Long permissionId) {
        groupService.addPermission(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> removePermission(@PathVariable Long groupId, @PathVariable Long permissionId) {
        groupService.removePermission(groupId, permissionId);
        return ResponseEntity.noContent().build();
    }
}
