package com.gtech.food_api.api.V2.disassembler;

import com.gtech.food_api.api.V2.dto.input.CityInput;
import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.State;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityInputDisassemblerV2 {
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
