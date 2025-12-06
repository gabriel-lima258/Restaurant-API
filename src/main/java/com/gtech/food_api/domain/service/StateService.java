package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.repository.StateRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.StateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StateService {

    private static final String STATE_IN_USE_MESSAGE = "State with id %d cannot be deleted because it is in use";

    @Autowired
    private StateRepository stateRepository;

    @Transactional(readOnly = true)
    public List<State> listAll(){
        return stateRepository.findAll();
    }

    @Transactional
    public State save(State state) {
        return stateRepository.save(state);
    }

    @Transactional
    public State update(Long id, State state) {
        State entity = findOrFail(id);
        entity.setName(state.getName());
        return stateRepository.save(entity);
    }

    @Transactional
    public void delete(Long id){
        if (!stateRepository.existsById(id)) {
            throw new StateNotFoundException(id);
        }
        try {
            stateRepository.deleteById(id);
            // flush força a violação de FK ainda dentro da transação
            stateRepository.flush();
        } catch (DataIntegrityViolationException e) {   
            throw new EntityInUseException(String.format(STATE_IN_USE_MESSAGE, id));
        }
    }

    @Transactional(readOnly = true)
    public State findOrFail(Long stateId) {
        return stateRepository.findById(stateId).orElseThrow(()
                -> new StateNotFoundException(stateId));
    }
}
