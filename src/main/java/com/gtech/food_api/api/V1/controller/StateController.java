package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.StateDTOAssembler;
import com.gtech.food_api.api.disassembler.StateInputDisassembler;
import com.gtech.food_api.api.dto.StateDTO;
import com.gtech.food_api.api.dto.input.StateInput;
import com.gtech.food_api.api.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.service.StateService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/states", produces = MediaType.APPLICATION_JSON_VALUE)
public class StateController {

    @Autowired
    private StateService stateService;

    @Autowired
    private StateDTOAssembler stateDTOAssembler;

    @Autowired
    private StateInputDisassembler stateInputDisassembler;

    @GetMapping
    public ResponseEntity<CollectionModel<StateDTO>> listAll(){
        List<State> result = stateService.listAll();
        CollectionModel<StateDTO> dtoList = stateDTOAssembler.toCollectionModel(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDTO> findById(@PathVariable Long id) {
        State entity = stateService.findOrFail(id);
        StateDTO dto = stateDTOAssembler.toModelWithSelf(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<StateDTO> save(@RequestBody @Valid StateInput stateInput) {
        State state = stateInputDisassembler.copyToEntity(stateInput);
        State entity = stateService.save(state);
        StateDTO dto = stateDTOAssembler.toModel(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDTO> update(@PathVariable Long id, @RequestBody @Valid StateInput stateInput) {
        State entity = stateService.findOrFail(id);
        stateInputDisassembler.copyToDomainObject(stateInput, entity);
        stateService.save(entity);
        StateDTO dto = stateDTOAssembler.toModel(entity);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
