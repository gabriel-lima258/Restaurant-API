package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.StateDTOAssemblerV1;
import com.gtech.food_api.api.V1.disassembler.StateInputDisassemblerV1;
import com.gtech.food_api.api.V1.dto.StateDTO;
import com.gtech.food_api.api.V1.dto.input.StateInput;
import com.gtech.food_api.api.V1.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.service.StateService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/states", produces = MediaType.APPLICATION_JSON_VALUE)
public class StateControllerV1 {

    @Autowired
    private StateService stateService;

    @Autowired
    private StateDTOAssemblerV1 stateDTOAssembler;

    @Autowired
    private StateInputDisassemblerV1 stateInputDisassembler;

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
        State state = stateInputDisassembler.copyToEntity(stateInput);
        State entity = stateService.save(state);
        StateDTO dto = stateDTOAssembler.copyToDTO(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StateDTO> update(@PathVariable Long id, @RequestBody @Valid StateInput stateInput) {
        State entity = stateService.findOrFail(id);
        stateInputDisassembler.copyToDomainObject(stateInput, entity);
        stateService.save(entity);
        StateDTO dto = stateDTOAssembler.copyToDTO(entity);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
