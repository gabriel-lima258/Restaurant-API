package com.gtech.food_api.api.V1.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import com.gtech.food_api.api.V1.dto.CityDTO;
import com.gtech.food_api.domain.model.City;


@Component
public class CityDTOAssemblerV1 {

    @Autowired
    private ModelMapper modelMapper;

    public CityDTO copyToDTO(City city) {
        return modelMapper.map(city, CityDTO.class);
    }

    public List<CityDTO> toCollectionDTO(Collection<City> cities) {
        return cities.stream()
            .map(this::copyToDTO)
            .collect(Collectors.toList());
    }
}
