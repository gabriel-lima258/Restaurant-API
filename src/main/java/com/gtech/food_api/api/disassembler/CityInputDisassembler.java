package com.gtech.food_api.api.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.model.input.CityInput;
import com.gtech.food_api.domain.model.City;

@Component
public class CityInputDisassembler {
    @Autowired
    private ModelMapper modelMapper;

    public City copyToDomainObject(CityInput cityInput) {
        return modelMapper.map(cityInput, City.class);
    }
}
