package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.GroupDTOAssemblerV1;
import com.gtech.food_api.api.V1.disassembler.GroupInputDisassemblerV1;
import com.gtech.food_api.api.V1.dto.GroupDTO;
import com.gtech.food_api.api.V1.dto.input.GroupInput;
import com.gtech.food_api.api.V1.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.service.GroupService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupControllerV1 {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupDTOAssemblerV1 groupDTOAssembler;

    @Autowired
    private GroupInputDisassemblerV1 groupInputDisassembler;

    @GetMapping
    public ResponseEntity<List<GroupDTO>> listAll(){
        List<Group> result = groupService.listAll();
        List<GroupDTO> dtoList = groupDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> findById(@PathVariable Long id) {
        Group entity = groupService.findOrFail(id);
        GroupDTO dto = groupDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<GroupDTO> save(@RequestBody @Valid GroupInput groupInput) {
        Group group = groupInputDisassembler.copyToEntity(groupInput);
        Group entity = groupService.save(group);
        GroupDTO dto = groupDTOAssembler.copyToDTO(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> update(@PathVariable Long id, @RequestBody @Valid GroupInput groupInput) {
        Group entity = groupService.findOrFail(id);
        groupInputDisassembler.copyToDomainObject(groupInput, entity);
        groupService.save(entity);
        GroupDTO dto = groupDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
