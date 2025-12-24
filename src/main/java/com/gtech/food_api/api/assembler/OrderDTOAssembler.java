package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.controller.CityController;
import com.gtech.food_api.api.controller.OrderController;
import com.gtech.food_api.api.controller.PaymentMethodController;
import com.gtech.food_api.api.controller.ProductController;
import com.gtech.food_api.api.controller.RestaurantController;
import com.gtech.food_api.api.controller.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import com.gtech.food_api.api.dto.OrderDTO;
import com.gtech.food_api.domain.model.Order;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Assembler para converter Kitchen em KitchenDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Order em um DTO OrderDTO
 */
@Component
public class OrderDTOAssembler extends RepresentationModelAssemblerSupport<Order, OrderDTO> {

    @Autowired
    private ModelMapper modelMapper;

    public OrderDTOAssembler() {
        super(OrderController.class, OrderDTO.class);
    }

    @Override
    public OrderDTO toModel(Order order) {
        OrderDTO orderDTO = createModelWithId(order.getCode(), order);
        modelMapper.map(order, orderDTO);

        orderDTO.add(linkTo(OrderController.class).withRel("orders"));

        orderDTO.getRestaurant().add(linkTo(methodOn(RestaurantController.class).findById(order.getRestaurant().getId())).withSelfRel());

        orderDTO.getClient().add(linkTo(methodOn(UserController.class).findById(order.getClient().getId())).withSelfRel());

        orderDTO.getPaymentMethod().add(linkTo(methodOn(PaymentMethodController.class).findById(order.getPaymentMethod().getId())).withSelfRel());

        orderDTO.getDeliveryAddress().getCity().add(linkTo(methodOn(CityController.class).findById(order.getDeliveryAddress().getCity().getId())).withSelfRel());

        orderDTO.getItems().forEach(item -> item.add(linkTo(methodOn(ProductController.class).findById(item.getProductId(), orderDTO.getRestaurant().getId())).withRel("product")));
        return orderDTO;
    }

    // public OrderDTO copyToDTO(Order order) {
    //     // Order -> OrderDTO
    //     return modelMapper.map(order, OrderDTO.class);
    // }

    // public List<OrderDTO> toCollectionDTO(Collection<Order> orders) {
    //     return orders.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
