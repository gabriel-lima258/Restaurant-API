package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.StateDTOAssemblerV2;
import com.gtech.food_api.api.V2.disassembler.StateInputDisassemblerV2;
import com.gtech.food_api.api.V2.dto.StateDTO;
import com.gtech.food_api.api.V2.dto.input.StateInput;
import com.gtech.food_api.api.V2.utils.ResourceUriHelper;
import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.service.StateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v2/states", produces = MediaType.APPLICATION_JSON_VALUE)
public class StateControllerV2 {

    @Autowired
    private StateService stateService;

    @Autowired
    private StateDTOAssemblerV2 stateDTOAssembler;

    @Autowired
    private StateInputDisassemblerV2 stateInputDisassembler;

    @CheckSecurity.States.CanView
    @GetMapping
    public ResponseEntity<CollectionModel<StateDTO>> listAll(){
        List<State> result = stateService.listAll();
        CollectionModel<StateDTO> dtoList = stateDTOAssembler.toCollectionModel(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @CheckSecurity.States.CanView
    @GetMapping("/{id}")
    public ResponseEntity<StateDTO> findById(@PathVariable Long id) {
        State entity = stateService.findOrFail(id);
        StateDTO dto = stateDTOAssembler.toModelWithSelf(entity);
        return ResponseEntity.ok().body(dto);
    }

    @CheckSecurity.States.CanEdit
    @PostMapping
    public ResponseEntity<StateDTO> save(@RequestBody @Valid StateInput stateInput) {
        State state = stateInputDisassembler.copyToEntity(stateInput);
        State entity = stateService.save(state);
        StateDTO dto = stateDTOAssembler.toModel(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        return ResponseEntity.created(uri).body(dto);
    }

    @CheckSecurity.States.CanEdit
    @PutMapping("/{id}")
    public ResponseEntity<StateDTO> update(@PathVariable Long id, @RequestBody @Valid StateInput stateInput) {
        State entity = stateService.findOrFail(id);
        stateInputDisassembler.copyToDomainObject(stateInput, entity);
        stateService.save(entity);
        StateDTO dto = stateDTOAssembler.toModel(entity);

        return ResponseEntity.ok().body(dto);
    }

    @CheckSecurity.States.CanEdit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
