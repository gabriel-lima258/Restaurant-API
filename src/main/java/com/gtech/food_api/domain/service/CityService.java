package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.repository.CityRepository;
import com.gtech.food_api.domain.repository.StateRepository;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateRepository stateRepository;

    @Transactional(readOnly = true)
    public List<City> listAll(){
        return cityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public City findById(Long id){
        return cityRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(String.format("City with id %s does not exist", id)));
    }

    @Transactional
    public City save(City city) {
        City entity = new City();
        entity.setName(city.getName());

        State state = stateRepository.findById(city.getState().getId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("State with id %s does not exist", city.getState().getId()))
        );

        entity.setState(state);
        return cityRepository.save(entity);
    }

    @Transactional
    public City update(Long id, City city) {
        try {
            City entity = findById(id);
            entity.setName(city.getName());

            State state = stateRepository.findById(city.getState().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(String.format("State with id %s does not exist", city.getState().getId()))
            );

            entity.setState(state);
            return cityRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("City with id %s does not exist", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format("City with id %s does not exist", id));
        }
        try {
            cityRepository.deleteById(id);
        }  catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format("City with id %s cannot be deleted because it is in use", id));
        }
    }
}
