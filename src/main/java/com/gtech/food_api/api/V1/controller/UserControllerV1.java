package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.UserDTOAssemblerV1;
import com.gtech.food_api.api.V1.disassembler.UserInputDisassemblerV1;
import com.gtech.food_api.api.V1.dto.UserDTO;
import com.gtech.food_api.api.V1.dto.input.UserInput;
import com.gtech.food_api.api.V1.dto.input.UserPasswordInput;
import com.gtech.food_api.api.V1.dto.input.UserWithPasswordInput;
import com.gtech.food_api.api.V1.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserControllerV1 {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDTOAssemblerV1 userDTOAssembler;

    @Autowired
    private UserInputDisassemblerV1 userInputDisassembler;

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
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
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
