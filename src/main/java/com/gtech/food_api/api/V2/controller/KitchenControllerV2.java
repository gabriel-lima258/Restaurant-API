package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.KitchenDTOAssemblerV2;
import com.gtech.food_api.api.V2.disassembler.KitchenInputDisassemblerV2;
import com.gtech.food_api.api.V2.dto.KitchenDTO;
import com.gtech.food_api.api.V2.dto.input.KitchenInput;
import com.gtech.food_api.api.V2.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.service.KitchenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/v2/kitchens", produces = MediaType.APPLICATION_JSON_VALUE)
public class KitchenControllerV2 {

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private KitchenDTOAssemblerV2 kitchenDTOAssembler;

    @Autowired
    private KitchenInputDisassemblerV2 kitchenInputDisassembler;

    // PagedResourcesAssembler é um componente do Spring HATEOAS que ajuda a criar uma paginação de recursos
    @Autowired
    private PagedResourcesAssembler<Kitchen> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<PagedModel<KitchenDTO>> listAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Kitchen> kitchens = kitchenService.listAll(pageable);

        // toModel é um método do PagedResourcesAssembler que cria um PagedModel a partir de uma lista de recursos e um assembler
        PagedModel<KitchenDTO> pagedModel = pagedResourcesAssembler.toModel(kitchens, kitchenDTOAssembler);

        return ResponseEntity.ok().body(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitchenDTO> findById(@PathVariable Long id) {
        Kitchen entity = kitchenService.findOrFail(id);
        KitchenDTO dto = kitchenDTOAssembler.toModel(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<KitchenDTO> save(@RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen kitchen = kitchenInputDisassembler.copyToEntity(kitchenInput);
        Kitchen entity = kitchenService.save(kitchen);
        KitchenDTO dto = kitchenDTOAssembler.toModel(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitchenDTO> update(@PathVariable Long id, @RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen entity = kitchenService.findOrFail(id);
        kitchenInputDisassembler.copyToDomainObject(kitchenInput, entity);
        kitchenService.save(entity);
        KitchenDTO dto = kitchenDTOAssembler.toModel(entity);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitchenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
