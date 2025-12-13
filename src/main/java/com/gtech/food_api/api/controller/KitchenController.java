package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.model.KitchenDTO;
import com.gtech.food_api.api.assembler.KitchenDTOAssembler;
import com.gtech.food_api.api.disassembler.KitchenInputDisassembler;
import com.gtech.food_api.api.model.input.KitchenInput;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.service.KitchenService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/kitchens")
public class KitchenController {

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private KitchenDTOAssembler kitchenDTOAssembler;

    @Autowired
    private KitchenInputDisassembler kitchenInputDisassembler;

    @GetMapping
    public ResponseEntity<List<KitchenDTO>> listAll(){
        List<Kitchen> result = kitchenService.listAll();
        List<KitchenDTO> dtoList = kitchenDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitchenDTO> findById(@PathVariable Long id) {
        Kitchen entity = kitchenService.findOrFail(id);
        KitchenDTO dto = kitchenDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<KitchenDTO> save(@RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen kitchen = kitchenInputDisassembler.copyToDomainObject(kitchenInput);
        Kitchen entity = kitchenService.save(kitchen);
        KitchenDTO dto = kitchenDTOAssembler.copyToDTO(entity);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(kitchen.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitchenDTO> update(@PathVariable Long id, @RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen kitchen = kitchenInputDisassembler.copyToDomainObject(kitchenInput);
        Kitchen entity = kitchenService.update(id, kitchen);
        KitchenDTO dto = kitchenDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitchenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
