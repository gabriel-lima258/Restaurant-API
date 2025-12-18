package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.KitchenDTOAssembler;
import com.gtech.food_api.api.disassembler.KitchenInputDisassembler;
import com.gtech.food_api.api.dto.KitchenDTO;
import com.gtech.food_api.api.dto.input.KitchenInput;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.service.KitchenService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    /**
     * Lista todas as cozinhas paginadas
     * @param pageable
     * @return Page<KitchenDTO>
     */ 
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
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();
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
