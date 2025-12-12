package com.gtech.food_api.core.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gtech.food_api.api.model.CityMixin;
import com.gtech.food_api.api.model.RestaurantMixin;
import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.api.model.KitchenMixin;
import com.gtech.food_api.api.model.StateMixin;
import com.gtech.food_api.domain.model.State;

/**
 * Módulo Jackson que registra os Mixins da aplicação.
 * 
 * O que faz:
 * - Registra RestaurantMixin para a entidade Restaurant
 * - Quando Restaurant é serializado, o Jackson aplica as regras do RestaurantMixin
 * - Permite isolar configurações JSON sem modificar a entidade de domínio
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este módulo no ObjectMapper
 * - setMixInAnnotation: vincula Restaurant.class com RestaurantMixin.class
 * - O Jackson "mistura" as anotações do mixin com a entidade durante serialização
 */
@Component
public class JacksonMixinModule extends SimpleModule {
    
    public JacksonMixinModule() {
        setMixInAnnotation(Restaurant.class, RestaurantMixin.class);
        setMixInAnnotation(City.class, CityMixin.class);
        setMixInAnnotation(Kitchen.class, KitchenMixin.class);
        setMixInAnnotation(State.class, StateMixin.class);
    }
}
