package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.repository.RestaurantRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    private static final String RESTAURANT_IN_USE_MESSAGE = "Restaurant with id %d cannot be deleted because it is in use";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private KitchenService kitchenService;

    @Transactional(readOnly = true)
    public List<Restaurant> listAll(){
        return restaurantRepository.findAll();
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        Long kitchenId = restaurant.getKitchen().getId();
        Kitchen kitchen = kitchenService.findOrFail(kitchenId);
        restaurant.setKitchen(kitchen);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void delete(Long id){
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }
        try {
            restaurantRepository.deleteById(id);
            // flush descarrega a transação no banco, caso de exception, o rollback é feito automaticamente, diferente de commit, que é o final é feito alteracao no banco
            restaurantRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(RESTAURANT_IN_USE_MESSAGE, id));
        }
    }

    @Transactional
    public void activate(Long restaurantId) {
        Restaurant restaurant = findOrFail(restaurantId);
        // ativa, nao precisa chamar o save, pois o jpa ja atualiza automaticamente
        restaurant.activate();
    }

    @Transactional
    public void deactivate(Long restaurantId) {
        Restaurant restaurant = findOrFail(restaurantId);
        restaurant.deactivate();
    }


    @Transactional(readOnly = true)
    public Restaurant findOrFail(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(()
                -> new RestaurantNotFoundException(restaurantId));
    }
}
