package com.gtech.food_api.domain.service;

import com.gtech.food_api.domain.model.City;
import com.gtech.food_api.domain.model.Order;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.repository.OrderRepository;
import com.gtech.food_api.domain.service.exceptions.PaymentNoAcceptedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubmitOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private UserService userService;

    @Autowired
    private CityService cityService;

    @Autowired
    private ProductService productService;

    @Transactional
    public Order submitOrder(Order order) {
        validateOrder(order);
        validateItems(order);

        order.setFeeShipping(order.getRestaurant().getShippingFee());
        order.calculateTotalValue(); // subtotal + feeShipping

        return orderRepository.save(order);
    }

    public void validateOrder(Order order) {
        // busca os objetos necessários 
        Restaurant restaurant = restaurantService.findOrFail(order.getRestaurant().getId());
        User user = userService.findOrFail(order.getClient().getId());
        PaymentMethod paymentMethod = paymentMethodService.findOrFail(order.getPaymentMethod().getId());
        City city = cityService.findOrFail(order.getDeliveryAddress().getCity().getId());

        // seta os objetos necessários no pedido
        order.setRestaurant(restaurant);
        order.setPaymentMethod(paymentMethod);
        order.getDeliveryAddress().setCity(city);
        order.setClient(user);

        // verifica se o restaurante não aceita o método de pagamento
        if (restaurant.notAcceptPaymentMethod(paymentMethod)) {
            throw new PaymentNoAcceptedException(paymentMethod.getDescription(), restaurant.getName());
        }
    }

    public void validateItems(Order order) {
        // verifica se os itens do pedido são válidos
        order.getItems().forEach(item -> {
            // busca o produto, passando o id do produto e o id do restaurante
            Product product = productService.findOrFail(
                item.getProduct().getId(), order.getRestaurant().getId());

            item.setProduct(product); // seta o produto no item
            item.setOrder(order); // seta o pedido no item
            item.setUnitPrice(product.getPrice()); // seta o preço unitário do produto no item
        }); 
    }

}
