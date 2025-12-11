package com.gtech.food_api.api.controller;

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

    @GetMapping
    public ResponseEntity<List<Kitchen>> listAll(){
        List<Kitchen> result = kitchenService.listAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kitchen> findById(@PathVariable Long id) {
        Kitchen entity = kitchenService.findOrFail(id);
        return ResponseEntity.ok().body(entity);
    }

    @PostMapping
    public ResponseEntity<Kitchen> save(@RequestBody @Valid Kitchen kitchen) {
        Kitchen entity = kitchenService.save(kitchen);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(kitchen.getId()).toUri();
        return ResponseEntity.created(uri).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kitchen> update(@PathVariable Long id, @RequestBody @Valid Kitchen kitchen) {
        Kitchen entity = kitchenService.update(id, kitchen);
        return ResponseEntity.ok().body(entity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitchenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
