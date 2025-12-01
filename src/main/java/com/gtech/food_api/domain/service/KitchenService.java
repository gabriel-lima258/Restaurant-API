package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.repository.KitchenRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.KitchenNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KitchenService {

    @Autowired
    private KitchenRepository kitchenRepository;

    @Transactional(readOnly = true)
    public List<Kitchen> listAll(){
        return kitchenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Kitchen findById(Long id){
        return kitchenRepository.findById(id).orElseThrow(()
                -> new KitchenNotFoundException(String.format("Kitchen with id %s does not exist", id)));
    }

    @Transactional
    public Kitchen save(Kitchen kitchen) {
        Kitchen entity = new Kitchen();
        entity.setName(kitchen.getName());
        kitchenRepository.save(entity);
        return kitchenRepository.save(entity);
    }

    @Transactional
    public Kitchen update(Long id, Kitchen kitchen) {
        try {
            Kitchen entity = findById(id);
            entity.setName(kitchen.getName());
            return kitchenRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new KitchenNotFoundException(String.format("Kitchen with id %s does not exist", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!kitchenRepository.existsById(id)) {
            throw new KitchenNotFoundException(String.format("Kitchen with id %s does not exist", id));
        }
        try {
            kitchenRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format("Kitchen with id %s cannot be deleted because it is in use", id));
        }
    }
}
