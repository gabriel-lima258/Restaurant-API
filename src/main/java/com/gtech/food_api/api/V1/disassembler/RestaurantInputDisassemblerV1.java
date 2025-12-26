package com.gtech.food_api.api.V1.disassembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.V1.dto.input.RestaurantInput;
import com.gtech.food_api.domain.model.City;
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
public class RestaurantInputDisassemblerV1 {

    @Autowired
    private ModelMapper modelMapper;

    public Restaurant copyToEntity(RestaurantInput restaurantInput) {
        // RestaurantInput -> Restaurant
        return modelMapper.map(restaurantInput, Restaurant.class);
    }

    public void copyToDomainObject(RestaurantInput restaurantInput, Restaurant restaurant) {
        /**
         * Para evitar erro de trocar a kitchen existente pela nova kitchen
         * e para evitar erro de trocar a city existente pela nova city
         * quando o restaurante é atualizado
         * temos que criar novas instancias de Kitchen e City
         * para evitar que o jpa confunda a kitchen e city existente com a nova kitchen e city
         * quando o restaurante é atualizado
         */
        restaurant.setKitchen(new Kitchen());
        if (restaurant.getAddress() != null) {
            restaurant.getAddress().setCity(new City());
        }
        // RestaurantInput -> Restaurant em update
        modelMapper.map(restaurantInput, restaurant);
    }
    
}
