package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.OrderControllerV2;
import com.gtech.food_api.api.V2.dto.OrderSummaryDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.UsersJwtSecurity;
import com.gtech.food_api.domain.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter Order em OrderSummaryDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Order em um DTO OrderDTO
 */
@Component
public class OrderSummaryDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Order, OrderSummaryDTO> {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired  
    private UsersJwtSecurity usersJwtSecurity;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;

    public OrderSummaryDTOAssemblerV2() {
        super(OrderControllerV2.class, OrderSummaryDTO.class);
    }
    @Override
    public OrderSummaryDTO toModel(Order order) {
        OrderSummaryDTO orderSummaryDTO = createModelWithId(order.getCode(), order);
        modelMapper.map(order, orderSummaryDTO);
        if (usersJwtSecurity.canViewRestaurants()) {
            orderSummaryDTO.getRestaurant().add(linksBuilder.linkToRestaurant(orderSummaryDTO.getRestaurant().getId()));
        }
        
        orderSummaryDTO.getClient().add(linksBuilder.linkToUser(orderSummaryDTO.getClient().getId()));

        return orderSummaryDTO;
    }
}
