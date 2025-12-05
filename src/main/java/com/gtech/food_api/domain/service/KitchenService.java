package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.repository.KitchenRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KitchenService {

    private static final String KITCHEN_NOT_FOUND_MESSAGE = "Kitchen with id %d does not exist";
    private static final String KITCHEN_IN_USE_MESSAGE = "Kitchen with id %d cannot be deleted because it is in use";

    @Autowired
    private KitchenRepository kitchenRepository;

    @Transactional(readOnly = true)
    public List<Kitchen> listAll(){
        return kitchenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Kitchen findById(Long id){
        return findOrFail(id);
    }

    @Transactional
    public Kitchen save(Kitchen kitchen) {
        return kitchenRepository.save(kitchen);
    }

    @Transactional
    public Kitchen update(Long id, Kitchen kitchen) {
        Kitchen entity = findOrFail(id);
        entity.setName(kitchen.getName());
        return kitchenRepository.save(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!kitchenRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(KITCHEN_NOT_FOUND_MESSAGE, id));
        }
        try {
            kitchenRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(KITCHEN_IN_USE_MESSAGE, id));
        }
    }

    public Kitchen findOrFail(Long kitchenId) {
        return kitchenRepository.findById(kitchenId).orElseThrow(()
                -> new ResourceNotFoundException(String.format(KITCHEN_NOT_FOUND_MESSAGE, kitchenId)));
    }
}
