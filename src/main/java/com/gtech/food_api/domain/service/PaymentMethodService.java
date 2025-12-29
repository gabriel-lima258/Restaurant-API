package com.gtech.food_api.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.repository.PaymentMethodRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.PaymentNotFoundException;

@Service
public class PaymentMethodService {

    private static final String PAYMENT_METHOD_IN_USE_MESSAGE = "Método de pagamento com id %d não pode ser excluído pois está em uso";
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional(readOnly = true)
    public List<PaymentMethod> listAll(){
        return paymentMethodRepository.findAll();
    }

    @Transactional
    public PaymentMethod save(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    @Transactional
    public void delete(Long paymentMethodId) {
        if (!paymentMethodRepository.existsById(paymentMethodId)) {
            throw new PaymentNotFoundException(paymentMethodId);
        }
        try {
            paymentMethodRepository.deleteById(paymentMethodId);
            paymentMethodRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(PAYMENT_METHOD_IN_USE_MESSAGE, paymentMethodId));
        }
    }

    @Transactional(readOnly = true)
    public PaymentMethod findOrFail(Long paymentMethodId) {
        return paymentMethodRepository.findById(paymentMethodId).orElseThrow(()
                -> new PaymentNotFoundException(paymentMethodId));
    }
}
