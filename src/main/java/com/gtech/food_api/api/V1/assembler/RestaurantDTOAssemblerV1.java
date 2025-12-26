package com.gtech.food_api.api.V1.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.gtech.food_api.api.V1.dto.RestaurantDTO;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter Restaurant em RestaurantDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Restaurant em um DTO RestaurantDTO
 * - toCollectionDTO: converte uma lista de entidades Restaurant em uma lista de DTOs RestaurantDTO
 */
@Component
public class RestaurantDTOAssemblerV1 {

    @Autowired
    private ModelMapper modelMapper;

    public RestaurantDTO copyToDTO(Restaurant restaurant) {
        // Restaurant -> RestaurantDTO
        return modelMapper.map(restaurant, RestaurantDTO.class);
    }
    
    public List<RestaurantDTO> toCollectionDTO(Collection<Restaurant> restaurants) {
        // List<Restaurant> -> List<RestaurantDTO>
        return restaurants.stream()
            .map(entity -> copyToDTO(entity))
            .collect(Collectors.toList());
    }
}
