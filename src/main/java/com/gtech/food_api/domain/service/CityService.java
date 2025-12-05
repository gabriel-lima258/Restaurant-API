package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.State;
import com.gtech.food_api.domain.repository.CityRepository;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CityService {

    private static final String CITY_NOT_FOUND_MESSAGE = "City with id %d does not exist";
    private static final String CITY_IN_USE_MESSAGE = "City with id %d cannot be deleted because it is in use";
    
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StateService stateService;

    @Transactional(readOnly = true)
    public List<City> listAll(){
        return cityRepository.findAll();
    }

    @Transactional
    public City save(City city) {
        // Busca o estado pelo service para evitar duplicação de código
        Long stateId = city.getState().getId();
        State state = stateService.findOrFail(stateId);

        city.setState(state);

        return cityRepository.save(city);
    }

    @Transactional
    public City update(Long id, City city) {
        City entity = findOrFail(id);
        entity.setName(city.getName());

        Long stateId = city.getState().getId();
        State state = stateService.findOrFail(stateId);

        entity.setState(state);

        return cityRepository.save(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, id));
        }
        try {
            cityRepository.deleteById(id);
        }  catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(CITY_IN_USE_MESSAGE, id));
        }
    }

    @Transactional(readOnly = true)
    public City findOrFail(Long cityId) {
        return cityRepository.findById(cityId).orElseThrow(()
                -> new ResourceNotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, cityId)));
    }
}
