package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.GroupDTOAssembler;
import com.gtech.food_api.api.assembler.UserDTOAssembler;
import com.gtech.food_api.api.disassembler.GroupInputDisassembler;
import com.gtech.food_api.api.disassembler.UserInputDisassembler;
import com.gtech.food_api.api.dto.GroupDTO;
import com.gtech.food_api.api.dto.UserDTO;
import com.gtech.food_api.api.dto.input.GroupInput;
import com.gtech.food_api.api.dto.input.UserInput;
import com.gtech.food_api.api.dto.input.UserPasswordInput;
import com.gtech.food_api.api.dto.input.UserWithPasswordInput;
import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDTOAssembler userDTOAssembler;

    @Autowired
    private UserInputDisassembler userInputDisassembler;

    @GetMapping
    public ResponseEntity<List<UserDTO>> listAll(){
        List<User> result = userService.listAll();
        List<UserDTO> dtoList = userDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        User entity = userService.findOrFail(id);
        UserDTO dto = userDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody @Valid UserWithPasswordInput userWithPasswordInput) {
        User user = userInputDisassembler.copyToEntity(userWithPasswordInput);
        User entity = userService.save(user);
        UserDTO dto = userDTOAssembler.copyToDTO(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody @Valid UserInput userInput) {
        User entity = userService.findOrFail(id);
        userInputDisassembler.copyToDomainObject(userInput, entity);
        userService.save(entity);
        UserDTO dto = userDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody @Valid UserPasswordInput userPasswordInput) {
        userService.updatePassword(id, userPasswordInput.getCurrentPassword(), userPasswordInput.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
