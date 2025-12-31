package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.GroupDTOAssemblerV2;
import com.gtech.food_api.api.V2.dto.GroupDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.UsersJwtSecurity;
import com.gtech.food_api.core.security.resource.CheckSecurity;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v2/users/{userId}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupControllerV2 {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupDTOAssemblerV2 groupDTOAssembler;

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;
   
    @CheckSecurity.UsersGroupsPermissions.CanView
    @GetMapping
    public ResponseEntity<CollectionModel<GroupDTO>> listAll(@PathVariable Long userId){
        User user = userService.findOrFail(userId);
        CollectionModel<GroupDTO> dtoList = groupDTOAssembler.toCollectionModel(user.getGroups())
            .removeLinks();
        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            dtoList.add(linksBuilder.linkToAssociatePermissionUser(userId));
            dtoList.getContent().forEach(groupDTO -> {
                groupDTO.add(linksBuilder.linkToRemovePermissionUser(userId, groupDTO.getId()));
            });
        }

        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PutMapping("/{groupId}")
    public ResponseEntity<Void> associateGroup(@PathVariable Long userId, @PathVariable Long groupId){
        userService.associateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> disassociateGroup(@PathVariable Long userId, @PathVariable Long groupId){
        userService.disassociateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
       
}
