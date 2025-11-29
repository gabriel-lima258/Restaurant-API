package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.repository.StateRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.StateNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StateService {

    @Autowired
    private StateRepository stateRepository;

    @Transactional(readOnly = true)
    public List<State> listAll(){
        return stateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public State findById(Long id){
        State entity = stateRepository.findById(id).orElseThrow(()
                -> new StateNotFoundException(String.format("State with id %s does not exist", id)));
        return entity;
    }

    @Transactional
    public State save(State state) {
        State entity = new State();
        entity.setName(state.getName());

        return stateRepository.save(entity);
    }

    @Transactional
    public State update(Long id, State state) {
        try {
            State entity = findById(id);
            entity.setName(state.getName());
            return stateRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new StateNotFoundException(String.format("State with id %s does not exist", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!stateRepository.existsById(id)) {
            throw new StateNotFoundException(String.format("State with id %s does not exist", id));
        }
        try {
            stateRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format("State with id %s cannot be deleted because it is in use", id));
        }
    }
}
