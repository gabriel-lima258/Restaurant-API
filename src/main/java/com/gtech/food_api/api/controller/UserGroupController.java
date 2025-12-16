package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.GroupDTOAssembler;
import com.gtech.food_api.api.dto.GroupDTO;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/groups")
public class UserGroupController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupDTOAssembler groupDTOAssembler;
   
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
