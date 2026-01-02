package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.assembler.PaymentMethodDTOAssemblerV2;
import com.gtech.food_api.api.V2.disassembler.PaymentMethodInputDisassemblerV2;
import com.gtech.food_api.api.V2.dto.PaymentMethodDTO;
import com.gtech.food_api.api.V2.dto.input.PaymentMethodInput;
import com.gtech.food_api.api.V2.openai.controller.PaymentMethodControllerOpenAi;
import com.gtech.food_api.api.V2.utils.ResourceUriHelper;
import com.gtech.food_api.core.security.resource.validations.CheckSecurity;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.service.PaymentMethodService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.StateNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/v2/payment-methods", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentMethodControllerV2 implements PaymentMethodControllerOpenAi {

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentMethodDTOAssemblerV2 paymentMethodDTOAssembler;

    @Autowired
    private PaymentMethodInputDisassemblerV2 paymentMethodInputDisassembler;

    @CheckSecurity.Payments.CanView
    @Override
    @GetMapping
    public ResponseEntity<CollectionModel<PaymentMethodDTO>> listAll(ServletWebRequest request){
        List<PaymentMethod> result = paymentMethodService.listAll();
        CollectionModel<PaymentMethodDTO> dtoList = paymentMethodDTOAssembler.toCollectionModel(result);

        /* CacheControl.maxAge(10, TimeUnit.SECONDS) é o tempo de expiração do cache em segundos.
         * CacheControl.cachePublic() é o tipo de cache público compartilhado por todos os clientes.
         * CacheControl.cachePrivate() é o tipo de cache privado local individual.
         * CacheControl.noCache() é o tipo de cache precisa validar ETag toda vez.
         * CacheControl.noStore() não existe cache.
         */
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .body(dtoList);
    }

    @CheckSecurity.Payments.CanView
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> findById(@PathVariable Long id) {
        PaymentMethod entity = paymentMethodService.findOrFail(id);
        PaymentMethodDTO dto = paymentMethodDTOAssembler.toModelWithSelf(entity);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .body(dto);
    }

    @CheckSecurity.Payments.CanEdit
    @Override
    @PostMapping
    public ResponseEntity<PaymentMethodDTO> save(@RequestBody @Valid PaymentMethodInput paymentMethodInput) {
        try {
            PaymentMethod paymentMethod = paymentMethodInputDisassembler.copyToEntity(paymentMethodInput);
            PaymentMethod entity = paymentMethodService.save(paymentMethod);
            PaymentMethodDTO dto = paymentMethodDTOAssembler.toModelWithSelf(entity);
            URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getId());
            return ResponseEntity.created(uri).body(dto);
        } catch (StateNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Payments.CanEdit
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> update(@PathVariable Long id, @RequestBody @Valid PaymentMethodInput paymentMethodInput) {
        try {
            PaymentMethod entity = paymentMethodService.findOrFail(id);
            paymentMethodInputDisassembler.copyToDomainObject(paymentMethodInput, entity);
            paymentMethodService.save(entity);
            PaymentMethodDTO dto = paymentMethodDTOAssembler.toModelWithSelf(entity);
            return ResponseEntity.ok().body(dto);
        } catch (StateNotFoundException e) { // existe, mas state id não existe
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Payments.CanEdit
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paymentMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
