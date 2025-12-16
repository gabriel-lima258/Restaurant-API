package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.model.User;
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

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Transactional(readOnly = true)
    public List<Restaurant> listAll(){
        return restaurantRepository.findAll();
    }

    @Transactional
    public Restaurant save(Restaurant restaurant) {
        Long kitchenId = restaurant.getKitchen().getId();
        Long cityId = restaurant.getAddress().getCity().getId();

        Kitchen kitchen = kitchenService.findOrFail(kitchenId);
        City city = cityService.findOrFail(cityId);

        restaurant.setKitchen(kitchen);
        restaurant.getAddress().setCity(city);
        
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


    // jpa sicroniza esses metodos com transactional no banco de dados, entao nao é necessario chamar o save
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

    @Transactional
    public void open(Long restaurantId) {
        Restaurant restaurant = findOrFail(restaurantId);
        restaurant.openRestaurant();
    }

    @Transactional
    public void close(Long restaurantId) {
        Restaurant restaurant = findOrFail(restaurantId);
        restaurant.closedRestaurant();
    }

    @Transactional
    public void disassociatePaymentMethod(Long restaurantId, Long paymentMethodId) {
        Restaurant restaurant = findOrFail(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findOrFail(paymentMethodId);

        restaurant.disassociatePaymentMethod(paymentMethod);
    }

    @Transactional
    public void associatePaymentMethod(Long restaurantId, Long paymentMethodId) {
        Restaurant restaurant = findOrFail(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findOrFail(paymentMethodId);

        restaurant.associatePaymentMethod(paymentMethod);
    }

    @Transactional
    public void addResponsible(Long restaurantId, Long userId) {
        Restaurant restaurant = findOrFail(restaurantId);
        User user = userService.findOrFail(userId);
        restaurant.addResponsible(user);
    }

    @Transactional
    public void removeResponsible(Long restaurantId, Long userId) {
        Restaurant restaurant = findOrFail(restaurantId);
        User user = userService.findOrFail(userId);
        restaurant.removeResponsible(user);
    }

    @Transactional(readOnly = true)
    public Restaurant findOrFail(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(()
                -> new RestaurantNotFoundException(restaurantId));
    }
}
