package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.repository.KitchenRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.KitchenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KitchenService {

    private static final String KITCHEN_IN_USE_MESSAGE = "Kitchen with id %d cannot be deleted because it is in use";

    @Autowired
    private KitchenRepository kitchenRepository;

    @Transactional(readOnly = true)
    public List<Kitchen> listAll(){
        return kitchenRepository.findAll();
    }

    @Transactional
    public Kitchen save(Kitchen kitchen) {
        return kitchenRepository.save(kitchen);
    }

    @Transactional
    public void delete(Long id){
        if (!kitchenRepository.existsById(id)) {
            throw new KitchenNotFoundException(id);
        }
        try {
            kitchenRepository.deleteById(id);
            // flush força a violação de FK ainda dentro da transação
            kitchenRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(KITCHEN_IN_USE_MESSAGE, id));
        }
    }

    @Transactional(readOnly = true)
    public Kitchen findOrFail(Long kitchenId) {
        return kitchenRepository.findById(kitchenId).orElseThrow(()
                -> new KitchenNotFoundException(kitchenId));
    }
}
