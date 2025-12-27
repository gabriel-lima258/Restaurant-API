package com.gtech.food_api.api.V1.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gtech.food_api.api.V1.dto.RestaurantSummaryDTO;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantSummaryDTOAssemblerV1 {

    @Autowired
    private ModelMapper modelMapper;

    public RestaurantSummaryDTO copyToDTO(Restaurant restaurant) {
        // Restaurant -> RestaurantSummaryDTO
        return modelMapper.map(restaurant, RestaurantSummaryDTO.class);
    }
    
    public List<RestaurantSummaryDTO> toCollectionDTO(Collection<Restaurant> restaurants) {
        // List<Restaurant> -> List<RestaurantSummaryDTO>
        return restaurants.stream()
            .map(entity -> copyToDTO(entity))
            .collect(Collectors.toList());
    }
}
