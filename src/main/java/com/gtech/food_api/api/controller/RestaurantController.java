package com.gtech.food_api.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.KitchenNotFoundException;
import com.gtech.food_api.domain.service.exceptions.StateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> listAll(){
        List<Restaurant> result = restaurantService.listAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findById(@PathVariable Long id) {
        Restaurant entity = restaurantService.findOrFail(id);
        return ResponseEntity.ok().body(entity);
    }

    @PostMapping
    public ResponseEntity<Restaurant> save(@RequestBody Restaurant restaurant) {
        try {
            Restaurant entity = restaurantService.save(restaurant);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(restaurant.getId()).toUri();
            return ResponseEntity.created(uri).body(entity);
        } catch (KitchenNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable Long id, @RequestBody Restaurant restaurant) {
        Restaurant entity = restaurantService.findOrFail(id);
        try {
            restaurantService.update(id, restaurant);
            return ResponseEntity.ok().body(entity);
        } catch (KitchenNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> fields) {
        Restaurant restaurant = restaurantService.findOrFail(id);
        merge(fields, restaurant);
        restaurantService.update(id, restaurant);
        return ResponseEntity.ok().build();
    }

    private void merge(Map<String, Object> fields, Restaurant restaurantDestination) {
        // usando jackson para converter o map para objeto restaurant
        ObjectMapper mapper = new ObjectMapper();
        Restaurant restaurantSource = mapper.convertValue(fields, Restaurant.class);

        fields.forEach((nameField, valueField) -> {
            // atributo da classe, busca o nome atributo
            Field field = ReflectionUtils.findField(Restaurant.class, nameField);
            field.setAccessible(true); // torna o atributo privado acessivel
            // buscando valor da propriedade
            Object valueObject = ReflectionUtils.getField(field, restaurantSource);
            // pegue o nameField e atualize a entidade inserindo o value do filed convertido pelo mapper
            ReflectionUtils.setField(field, restaurantDestination, valueObject);
        });
    }
}
