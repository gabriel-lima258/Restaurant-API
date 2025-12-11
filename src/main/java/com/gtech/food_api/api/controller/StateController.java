package com.gtech.food_api.api.controller;

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

    @GetMapping
    public ResponseEntity<List<State>> listAll(){
        List<State> result = stateService.listAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<State> findById(@PathVariable Long id) {
        State entity = stateService.findOrFail(id);
        return ResponseEntity.ok().body(entity);
    }

    @PostMapping
    public ResponseEntity<State> save(@RequestBody @Valid State state) {
        State entity = stateService.save(state);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(state.getId()).toUri();
        return ResponseEntity.created(uri).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<State> update(@PathVariable Long id, @RequestBody @Valid State state) {
        State entity = stateService.update(id, state);
        return ResponseEntity.ok().body(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
