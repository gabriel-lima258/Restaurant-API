package com.gtech.food_api.api.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.modelmapper.ModelMapper;

import com.gtech.food_api.api.dto.CityDTO;
import com.gtech.food_api.domain.model.City;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CityDTOAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public CityDTO copyToDTO(City city) {
        return modelMapper.map(city, CityDTO.class);
    }

    public List<CityDTO> toCollectionDTO(List<City> cities) {
        return cities.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
