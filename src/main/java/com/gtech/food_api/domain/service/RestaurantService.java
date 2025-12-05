package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.repository.RestaurantRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    private static final String RESTAURANT_NOT_FOUND_MESSAGE = "Restaurant with id %d does not exist";
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
    public Restaurant update(Long id, Restaurant restaurant) {
        Restaurant entity = findOrFail(id);
        entity.setName(restaurant.getName());
        entity.setShippingFee(restaurant.getShippingFee());

        Long kitchenId = restaurant.getKitchen().getId();
        Kitchen kitchen = kitchenService.findOrFail(kitchenId);

        entity.setKitchen(kitchen);

        return restaurantRepository.save(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format(RESTAURANT_NOT_FOUND_MESSAGE, id));
        }
        try {
            restaurantRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format(RESTAURANT_IN_USE_MESSAGE, id));
        }
    }

    @Transactional(readOnly = true)
    public Restaurant findOrFail(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(()
                -> new ResourceNotFoundException(String.format(RESTAURANT_NOT_FOUND_MESSAGE, restaurantId)));
    }
}
