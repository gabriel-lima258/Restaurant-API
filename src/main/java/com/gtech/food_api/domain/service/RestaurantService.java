package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.repository.KitchenRepository;
import com.gtech.food_api.domain.repository.RestaurantRepository;
import com.gtech.food_api.domain.service.exceptions.EntityInUseException;
import com.gtech.food_api.domain.service.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private KitchenRepository kitchenRepository;

    @Transactional(readOnly = true)
    public List<Restaurant> listAll(){
        return restaurantRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Restaurant findById(Long id){
        Restaurant entity = restaurantRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException(String.format("Restaurant with id %s does not exist", id)));
        return entity;
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        Restaurant entity = new Restaurant();
        entity.setName(restaurant.getName());
        entity.setShippingFee(restaurant.getShippingFee());

        Kitchen kitchen = kitchenRepository.findById(restaurant.getKitchen().getId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Kitchen with id %s does not exist", restaurant.getKitchen().getId()))
        );
        entity.setKitchen(kitchen);

        return restaurantRepository.save(entity);
    }

    @Transactional
    public Restaurant update(Long id, Restaurant restaurant) {
        try {
            Restaurant entity = findById(id);
            entity.setName(restaurant.getName());
            entity.setShippingFee(restaurant.getShippingFee());

            Kitchen kitchen = kitchenRepository.findById(restaurant.getKitchen().getId()).orElseThrow(
                    () -> new ResourceNotFoundException(String.format("Kitchen with id %s does not exist", restaurant.getKitchen().getId()))
            );

            entity.setKitchen(kitchen);

            return restaurantRepository.save(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Restaurant with id %s does not exist", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id){
        if (!restaurantRepository.existsById(id)) {
            throw new ResourceNotFoundException(String.format("Restaurant with id %s does not exist", id));
        }
        try {
            restaurantRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(String.format("Restaurant with id %s cannot be deleted because it is in use", id));
        }
    }
}
