package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.KitchenDTOAssemblerV1;
import com.gtech.food_api.api.V1.disassembler.KitchenInputDisassemblerV1;
import com.gtech.food_api.api.V1.dto.KitchenDTO;
import com.gtech.food_api.api.V1.dto.input.KitchenInput;
import com.gtech.food_api.api.V1.utils.ResourceUriHelper;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.service.KitchenService;

import org.springframework.data.domain.PageImpl;
import jakarta.validation.Valid;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
    
import java.net.URI;

@RestController
@RequestMapping(value = "/v1/kitchens", produces = MediaType.APPLICATION_JSON_VALUE)
public class KitchenControllerV1 {

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private KitchenDTOAssemblerV1 kitchenDTOAssembler;

    @Autowired
    private KitchenInputDisassemblerV1 kitchenInputDisassembler;


    @GetMapping
    public ResponseEntity<Page<KitchenDTO>> listAll(@PageableDefault(size = 10) Pageable pageable){
        Page<Kitchen> kitchens = kitchenService.listAll(pageable);

        List<KitchenDTO> content = kitchenDTOAssembler.toCollectionDTO(kitchens.getContent());

        /*
         * cria uma nova pagina com os dados da pagina atual e o total de elementos
         * content: lista de DTOs
         * pageable: pagina atual
         * kitchens.getTotalElements(): total de elementos
         */
        Page<KitchenDTO> kitchenPageDTO = new PageImpl<>(content, pageable, kitchens.getTotalElements());

        return ResponseEntity.ok().body(kitchenPageDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KitchenDTO> findById(@PathVariable Long id) {
        Kitchen entity = kitchenService.findOrFail(id);
        KitchenDTO dto = kitchenDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<KitchenDTO> save(@RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen kitchen = kitchenInputDisassembler.copyToEntity(kitchenInput);
        Kitchen entity = kitchenService.save(kitchen);
        KitchenDTO dto = kitchenDTOAssembler.copyToDTO(entity);
        URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KitchenDTO> update(@PathVariable Long id, @RequestBody @Valid KitchenInput kitchenInput) {
        Kitchen entity = kitchenService.findOrFail(id);
        kitchenInputDisassembler.copyToDomainObject(kitchenInput, entity);
        kitchenService.save(entity);
        KitchenDTO dto = kitchenDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kitchenService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
