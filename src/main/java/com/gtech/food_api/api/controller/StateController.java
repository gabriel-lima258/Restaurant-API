package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.StateDTOAssembler;
import com.gtech.food_api.api.disassembler.StateInputDisassembler;
import com.gtech.food_api.api.model.StateDTO;
import com.gtech.food_api.api.model.input.StateInput;
import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.service.StateService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/states")
public class StateController {

    @Autowired
    private StateService stateService;

    @Autowired
    private StateDTOAssembler stateDTOAssembler;

    @Autowired
    private StateInputDisassembler stateInputDisassembler;

    @GetMapping
    public ResponseEntity<List<StateDTO>> listAll(){
        List<State> result = stateService.listAll();
        List<StateDTO> dtoList = stateDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDTO> findById(@PathVariable Long id) {
        State entity = stateService.findOrFail(id);
        StateDTO dto = stateDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<StateDTO> save(@RequestBody @Valid StateInput stateInput) {
        State state = stateInputDisassembler.copyToDomainObject(stateInput);
        State entity = stateService.save(state);
        StateDTO dto = stateDTOAssembler.copyToDTO(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(state.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDTO> update(@PathVariable Long id, @RequestBody @Valid StateInput stateInput) {
        State state = stateInputDisassembler.copyToDomainObject(stateInput);
        State entity = stateService.update(id, state);
        StateDTO dto = stateDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
