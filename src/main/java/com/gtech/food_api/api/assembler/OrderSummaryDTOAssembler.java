package com.gtech.food_api.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.controller.OrderController;
import com.gtech.food_api.api.dto.OrderSummaryDTO;
import com.gtech.food_api.api.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Order;


/**
 * Assembler para converter Order em OrderSummaryDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Order em um DTO OrderDTO
 */
@Component
public class OrderSummaryDTOAssembler extends RepresentationModelAssemblerSupport<Order, OrderSummaryDTO> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public OrderSummaryDTOAssembler() {
        super(OrderController.class, OrderSummaryDTO.class);
    }

    @Override
    public OrderSummaryDTO toModel(Order order) {
        OrderSummaryDTO orderSummaryDTO = createModelWithId(order.getCode(), order);
        modelMapper.map(order, orderSummaryDTO);

        orderSummaryDTO.getRestaurant().add(linksBuilder.linkToRestaurant(orderSummaryDTO.getRestaurant().getId()));
        orderSummaryDTO.getClient().add(linksBuilder.linkToUser(orderSummaryDTO.getClient().getId()));

        return orderSummaryDTO;
    }

    // public OrderSummaryDTO copyToDTO(Order order) {
    //     // Order -> OrderDTO
    //     return modelMapper.map(order, OrderSummaryDTO.class);
    // }

    // public List<OrderSummaryDTO> toCollectionDTO(Collection<Order> orders) {
    //     return orders.stream()
    //         .map(this::copyToDTO)
    //         .collect(Collectors.toList());
    // }
}
