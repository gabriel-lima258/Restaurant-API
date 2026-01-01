package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.CityDTOAssemblerV2;
import com.gtech.food_api.api.V2.disassembler.CityInputDisassemblerV2;
import com.gtech.food_api.api.V2.dto.CityDTO;
import com.gtech.food_api.api.V2.dto.input.CityInput;
import com.gtech.food_api.api.V2.utils.ResourceUriHelper;
import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.service.CityService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.StateNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/v2/cities", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityControllerV2 {

    @Autowired
    private CityService cityService;

    @Autowired
    private CityDTOAssemblerV2 cityDTOAssembler;

    @Autowired
    private CityInputDisassemblerV2 cityInputDisassembler;

    // usamos CollectionModel para retornar uma lista de DTOs com links HATEOAS
    @CheckSecurity.Cities.CanView
    @GetMapping
    public ResponseEntity<CollectionModel<CityDTO>> listAll(){
        List<City> result = cityService.listAll();
        CollectionModel<CityDTO> dtoCollectionModel = cityDTOAssembler.toCollectionModel(result);

        return ResponseEntity.ok().body(dtoCollectionModel);
    }

    @CheckSecurity.Cities.CanView
    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> findById(@PathVariable Long id) {
        City entity = cityService.findOrFail(id);
        CityDTO dto = cityDTOAssembler.toModelWithSelf(entity);

        return ResponseEntity.ok().body(dto);
    }

    @CheckSecurity.Cities.CanEdit
    @PostMapping
    public ResponseEntity<CityDTO> save(@RequestBody @Valid CityInput cityInput) {
        try {
            City city = cityInputDisassembler.copyToEntity(cityInput);
            City entity = cityService.save(city);
            CityDTO dto = cityDTOAssembler.toModel(entity);
            URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());

            return ResponseEntity.created(uri).body(dto);
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Cities.CanEdit
    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> update(@PathVariable Long id, @RequestBody @Valid CityInput cityInput) {
        try {
            City entity = cityService.findOrFail(id);
            cityInputDisassembler.copyToDomainObject(cityInput, entity);
            cityService.save(entity);
            CityDTO dto = cityDTOAssembler.toModel(entity);
            return ResponseEntity.ok().body(dto);
        } catch (StateNotFoundException e) { // existe, mas state id não existe
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Cities.CanEdit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
