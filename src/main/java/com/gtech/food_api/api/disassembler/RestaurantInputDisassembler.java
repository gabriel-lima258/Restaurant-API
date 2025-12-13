package com.gtech.food_api.api.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.model.input.RestaurantInput;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.model.Restaurant;
import org.modelmapper.ModelMapper;

/**
 * Disassembler para converter RestaurantInput em Restaurant
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este disassembler
 * - copyToEntity: converte um DTO RestaurantInput em uma entidade Restaurant
 */
@Component
public class RestaurantInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Restaurant copyToEntity(RestaurantInput restaurantInput) {
        // RestaurantInput -> Restaurant
        return modelMapper.map(restaurantInput, Restaurant.class);
    }

    // public void copyToDomainObject(RestaurantInput restaurantInput, Restaurant restaurant) {
    //     // para evitar erro de trocar a kitchen existente pela nova kitchen
    //     restaurant.setKitchen(new Kitchen());
    //     // converte o RestaurantInput para Restaurant
    //     modelMapper.map(restaurantInput, restaurant);
    // }
    
}
