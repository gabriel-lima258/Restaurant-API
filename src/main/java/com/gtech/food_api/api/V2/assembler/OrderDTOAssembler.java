package com.gtech.food_api.api.V1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.gtech.food_api.api.V1.controller.OrderController;
import com.gtech.food_api.api.V1.dto.OrderDTO;
import com.gtech.food_api.api.V1.utils.LinksBuilder;
import com.gtech.food_api.domain.model.Order;

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

    @Autowired
    private LinksBuilder linksBuilder;

    public OrderDTOAssembler() {
        super(OrderController.class, OrderDTO.class);
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
