package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.GroupDTOAssemblerV1;
import com.gtech.food_api.api.V1.dto.GroupDTO;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/users/{userId}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserGroupControllerV1 {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupDTOAssemblerV1 groupDTOAssembler;

   
    @GetMapping
    public ResponseEntity<List<GroupDTO>> listAll(@PathVariable Long userId){
        User user = userService.findOrFail(userId);
        List<GroupDTO> dtoList = groupDTOAssembler.toCollectionDTO(user.getGroups());

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
