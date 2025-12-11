package com.gtech.food_api.api.controller;

import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.service.CityService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.StateNotFoundException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<City>> listAll(){
        List<City> result = cityService.listAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> findById(@PathVariable Long id) {
        City entity = cityService.findOrFail(id);
        return ResponseEntity.ok().body(entity);
    }

    @PostMapping
    public ResponseEntity<City> save(@RequestBody @Valid City state) {
        try {
            City entity = cityService.save(state);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(state.getId()).toUri();
            return ResponseEntity.created(uri).body(entity);
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> update(@PathVariable Long id, @RequestBody @Valid City state) {
        // city existe?
        City entity = cityService.findOrFail(id);
        try {
            cityService.update(id, state); // existe, atualiza
            return ResponseEntity.ok().body(entity);
        } catch (StateNotFoundException e) { // existe, mas state id não existe
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
