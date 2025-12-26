package com.gtech.food_api.api.V2.assembler;

import com.gtech.food_api.api.V2.controller.OrderControllerV2;
import com.gtech.food_api.api.V2.dto.OrderDTO;
import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.domain.model.Order;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
/**
 * Assembler para converter Kitchen em KitchenDTO
 * 
 * Como funciona:
 * - @Component: Spring detecta e registra automaticamente este assembler
 * - copyToDTO: converte uma entidade Order em um DTO OrderDTO
 */
@Component
public class OrderDTOAssemblerV2 extends RepresentationModelAssemblerSupport<Order, OrderDTO> {
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private LinksBuilderV2 linksBuilder;
    public OrderDTOAssemblerV2() {
        super(OrderControllerV2.class, OrderDTO.class);
    }
    @Override
    public OrderDTO toModel(Order order) {
        OrderDTO orderDTO = createModelWithId(order.getCode(), order);
        modelMapper.map(order, orderDTO);
        orderDTO.add(linksBuilder.linkToOrders("orders"));
        // adiciona links de confirmação, entrega e cancelamento se o pedido puder ser alterado para o status correspondente
        if (order.canBeConfirmed()) {
            orderDTO.add(linksBuilder.linkToConfimOrder(order.getCode(), "confirm"));
        }
        if (order.canBeDelivered()) {
            orderDTO.add(linksBuilder.linkToDeliverOrder(order.getCode(), "deliver"));
        }
        if (order.canBeCanceled()) {
            orderDTO.add(linksBuilder.linkToCancelOrder(order.getCode(), "cancel"));
        }
        orderDTO.getRestaurant().add(linksBuilder.linkToRestaurant(order.getRestaurant().getId()));
        orderDTO.getClient().add(linksBuilder.linkToUser(order.getClient().getId()));
        orderDTO.getPaymentMethod().add(linksBuilder.linkToPaymentMethod(order.getPaymentMethod().getId()));
        orderDTO.getDeliveryAddress().getCity().add(linksBuilder.linkToCity(order.getDeliveryAddress().getCity().getId()));
        orderDTO.getItems().forEach(item -> item.add(linksBuilder.linkToProduct(item.getProductId(), orderDTO.getRestaurant().getId(), "product")));
        return orderDTO;
    }
}
