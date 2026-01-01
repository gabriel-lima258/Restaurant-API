package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.GroupDTOAssemblerV2;
import com.gtech.food_api.api.V2.disassembler.GroupInputDisassemblerV2;
import com.gtech.food_api.api.V2.dto.GroupDTO;
import com.gtech.food_api.api.V2.dto.input.GroupInput;
import com.gtech.food_api.api.V2.utils.ResourceUriHelper;
import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.domain.model.Group;
import com.gtech.food_api.domain.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v2/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupControllerV2 {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupDTOAssemblerV2 groupDTOAssembler;

    @Autowired
    private GroupInputDisassemblerV2 groupInputDisassembler;

    @CheckSecurity.UsersGroupsPermissions.CanView
    @GetMapping
    public ResponseEntity<CollectionModel<GroupDTO>> listAll(){
        List<Group> result = groupService.listAll();
        CollectionModel<GroupDTO> dtoList = groupDTOAssembler.toCollectionModel(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.UsersGroupsPermissions.CanView
    @GetMapping("/{id}")
    public ResponseEntity<GroupDTO> findById(@PathVariable Long id) {
        Group entity = groupService.findOrFail(id);
        GroupDTO dto = groupDTOAssembler.toModelWithSelf(entity);
        return ResponseEntity.ok().body(dto);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PostMapping
    public ResponseEntity<GroupDTO> save(@RequestBody @Valid GroupInput groupInput) {
        Group group = groupInputDisassembler.copyToEntity(groupInput);
        Group entity = groupService.save(group);
        GroupDTO dto = groupDTOAssembler.toModelWithSelf(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        
        return ResponseEntity.created(uri).body(dto);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @PutMapping("/{id}")
    public ResponseEntity<GroupDTO> update(@PathVariable Long id, @RequestBody @Valid GroupInput groupInput) {
        Group entity = groupService.findOrFail(id);
        groupInputDisassembler.copyToDomainObject(groupInput, entity);
        groupService.save(entity);
        GroupDTO dto = groupDTOAssembler.toModelWithSelf(entity);
        return ResponseEntity.ok().body(dto);
    }

    @CheckSecurity.UsersGroupsPermissions.CanEdit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
