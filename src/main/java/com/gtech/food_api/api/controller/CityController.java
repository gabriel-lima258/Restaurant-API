package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.CityDTOAssembler;
import com.gtech.food_api.api.disassembler.CityInputDisassembler;
import com.gtech.food_api.api.dto.CityDTO;
import com.gtech.food_api.api.dto.input.CityInput;
import com.gtech.food_api.api.utils.ResourceUriHelper;
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

    @Autowired
    private CityDTOAssembler cityDTOAssembler;

    @Autowired
    private CityInputDisassembler cityInputDisassembler;

    @GetMapping
    public ResponseEntity<List<CityDTO>> listAll(){
        List<City> result = cityService.listAll();
        List<CityDTO> dtoList = cityDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
        City entity = cityService.findOrFail(id);
        CityDTO dto = cityDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<CityDTO> save(@RequestBody @Valid CityInput cityInput) {
        try {
            City city = cityInputDisassembler.copyToEntity(cityInput);
            City entity = cityService.save(city);
            CityDTO dto = cityDTOAssembler.copyToDTO(entity);
            URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());

            return ResponseEntity.created(uri).body(dto);
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> update(@PathVariable Long id, @RequestBody @Valid CityInput cityInput) {
        try {
            City entity = cityService.findOrFail(id);
            cityInputDisassembler.copyToDomainObject(cityInput, entity);
            cityService.save(entity);
            CityDTO dto = cityDTOAssembler.copyToDTO(entity);
            return ResponseEntity.ok().body(dto);
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
