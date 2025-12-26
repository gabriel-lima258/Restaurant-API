package com.gtech.food_api.api.V1.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.V1.dto.input.CityInput;
import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.State;

@Component
public class CityInputDisassembler {
    @Autowired
    private ModelMapper modelMapper;

    public City copyToEntity(CityInput cityInput) {
        return modelMapper.map(cityInput, City.class);
    }

    public void copyToDomainObject(CityInput cityInput, City city) {
        city.setState(new State());
        modelMapper.map(cityInput, city);
    }
}
