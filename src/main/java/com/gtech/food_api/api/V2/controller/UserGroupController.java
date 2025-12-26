package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.GroupDTOAssembler;
import com.gtech.food_api.api.V1.dto.GroupDTO;
import com.gtech.food_api.api.V1.utils.LinksBuilder;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/users/{userId}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupDTOAssembler groupDTOAssembler;

    @Autowired
    private LinksBuilder linksBuilder;
   
    @GetMapping
    public ResponseEntity<CollectionModel<GroupDTO>> listAll(@PathVariable Long userId){
        User user = userService.findOrFail(userId);
        CollectionModel<GroupDTO> dtoList = groupDTOAssembler.toCollectionModel(user.getGroups())
            .removeLinks()
            .add(linksBuilder.linkToAssociatePermissionUser(userId));

        dtoList.getContent().forEach(groupDTO -> {
            groupDTO.add(linksBuilder.linkToRemovePermissionUser(userId, groupDTO.getId()));
        });

        return ResponseEntity.ok().body(dtoList);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<Void> associateGroup(@PathVariable Long userId, @PathVariable Long groupId){
        userService.associateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> disassociateGroup(@PathVariable Long userId, @PathVariable Long groupId){
        userService.disassociateGroup(userId, groupId);
        return ResponseEntity.noContent().build();
    }
       
}
