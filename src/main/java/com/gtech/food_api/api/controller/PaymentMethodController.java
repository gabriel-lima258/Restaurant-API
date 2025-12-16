package com.gtech.food_api.api.controller;

import com.gtech.food_api.api.assembler.PaymentMethodDTOAssembler;
import com.gtech.food_api.api.disassembler.PaymentMethodInputDisassembler;
import com.gtech.food_api.api.dto.PaymentMethodDTO;
import com.gtech.food_api.api.dto.input.PaymentMethodInput;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.service.PaymentMethodService;
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
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentMethodDTOAssembler paymentMethodDTOAssembler;

    @Autowired
    private PaymentMethodInputDisassembler paymentMethodInputDisassembler;

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> listAll(){
        List<PaymentMethod> result = paymentMethodService.listAll();
        List<PaymentMethodDTO> dtoList = paymentMethodDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> findById(@PathVariable Long id) {
        PaymentMethod entity = paymentMethodService.findOrFail(id);
        PaymentMethodDTO dto = paymentMethodDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> save(@RequestBody @Valid PaymentMethodInput paymentMethodInput) {
        try {
            PaymentMethod paymentMethod = paymentMethodInputDisassembler.copyToEntity(paymentMethodInput);
            PaymentMethod entity = paymentMethodService.save(paymentMethod);
            PaymentMethodDTO dto = paymentMethodDTOAssembler.copyToDTO(entity);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(entity.getId()).toUri();
            return ResponseEntity.created(uri).body(dto);
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> update(@PathVariable Long id, @RequestBody @Valid PaymentMethodInput paymentMethodInput) {
        try {
            PaymentMethod entity = paymentMethodService.findOrFail(id);
            paymentMethodInputDisassembler.copyToDomainObject(paymentMethodInput, entity);
            paymentMethodService.save(entity);
            PaymentMethodDTO dto = paymentMethodDTOAssembler.copyToDTO(entity);
            return ResponseEntity.ok().body(dto);
        } catch (StateNotFoundException e) { // existe, mas state id não existe
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
