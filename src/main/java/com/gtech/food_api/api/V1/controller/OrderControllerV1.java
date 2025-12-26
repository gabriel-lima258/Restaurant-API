package com.gtech.food_api.api.V1.controller;

import com.gtech.food_api.api.V1.assembler.OrderDTOAssemblerV1;
import com.gtech.food_api.api.V1.assembler.OrderSummaryDTOAssemblerV1;
import com.gtech.food_api.api.V1.disassembler.OrderInputDisassemblerV1;
import com.gtech.food_api.api.V1.dto.OrderDTO;
import com.gtech.food_api.api.V1.dto.OrderSummaryDTO;
import com.gtech.food_api.api.V1.dto.input.OrderInput;
import com.gtech.food_api.api.V1.utils.ResourceUriHelper;
import com.gtech.food_api.core.data.PageWrapper;
import com.gtech.food_api.core.data.PageableTranslator;
import com.gtech.food_api.domain.filter.OrderFilter;
import com.gtech.food_api.domain.model.Order;
import java.util.List;  
import com.gtech.food_api.domain.model.User;
import com.gtech.food_api.domain.service.OrderService;
import com.gtech.food_api.domain.service.SubmitOrderService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import org.springframework.data.domain.PageImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderControllerV1 {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SubmitOrderService submitOrderService;

    @Autowired
    private OrderDTOAssemblerV1 orderDTOAssembler;

    @Autowired
    private OrderSummaryDTOAssemblerV1 orderSummaryDTOAssembler;

    @Autowired
    private OrderInputDisassemblerV1 orderInputDisassembler;
    /*
    * OrderFilter foi injetado no metodo listAll, para que seja possivel passar o filtro na url, exemplo: /orders?clientId=1&restaurantId=1&creationDateStart=2025-01-01&creationDateEnd=2025-01-01
    * @param filter: filtro de pedidos, exemplo: clientId, restaurantId, creationDateStart, creationDateEnd
    */
    @GetMapping
    public ResponseEntity<Page<OrderSummaryDTO>> listAll(OrderFilter filter,@PageableDefault(size = 10) Pageable pageable ){
        // traduz a paginação para o nome da entidade Ex: nameClient -> client.name
        Pageable pageableTranslated = convertPageable(pageable);

        // lista todos os pedidos com o filtro de specification e a paginação
        Page<Order> pagedOrders = orderService.listAll(filter, pageableTranslated);

        List<OrderSummaryDTO> dtoContent = orderSummaryDTOAssembler.toCollectionDTO(pagedOrders.getContent());

        Page<OrderSummaryDTO> orderSummaryPage = new PageImpl<>(dtoContent, pageable, pagedOrders.getTotalElements());

        return ResponseEntity.ok().body(orderSummaryPage);
    }

    @GetMapping("/{orderCode}")
    public ResponseEntity<OrderDTO> findById(@PathVariable String orderCode) {
        Order entity = orderService.findOrFail(orderCode);
        OrderDTO dto = orderDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> save(@RequestBody @Valid OrderInput orderInput) {
        try {
            Order order = orderInputDisassembler.copyToEntity(orderInput);

            // get user authenticated
            order.setClient(new User());
            order.getClient().setId(1L);

            Order newOrder = submitOrderService.submitOrder(order);
            OrderDTO dto = orderDTOAssembler.copyToDTO(newOrder);
            
            URI uri = ResourceUriHelper.addUriInResponseHeader(dto.getCode());

            return ResponseEntity.created(uri).body(dto);
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    /**
     * Converte os parâmetros de ordenação (sort) da URL para os nomes de propriedades da entidade.
     * 
     * Por que isso é necessário?
     * - O frontend pode usar nomes de campos do DTO (mais amigáveis) na URL
     * - O backend precisa usar os nomes de propriedades da entidade para ordenação no banco
     * - Isso mantém o desacoplamento entre a API (DTO) e o modelo de domínio (Entity)
     * 
     * Como funciona:
     * - Recebe um Pageable com parâmetros de ordenação da URL (ex: ?sort=nameClient,asc)
     * - Traduz os nomes dos campos usando o mapper (chave = nome da URL, valor = nome da entidade)
     * - Retorna um novo Pageable com os nomes de propriedades corretos para a entidade
     * 
     * Exemplos de uso na URL:
     * - GET /orders?sort=code,asc
     *   → Ordena por "code" (mesmo nome na entidade)
     * 
     * - GET /orders?sort=nameClient,desc
     *   → Traduz "nameClient" (DTO) para "client.name" (entidade)
     * 
     * - GET /orders?sort=restaurant.name,asc&sort=totalValue,desc
     *   → Ordena por nome do restaurante (asc) e depois por valor total (desc)
     * 
     * Mapeamento de campos:
     * - "code" → "code" (mesmo nome)
     * - "nameRestaurant" → "restaurant.name" (tradução: DTO usa nameRestaurant, entidade usa restaurant.name)
     * - "nameClient" → "client.name" (tradução: DTO usa nameClient, entidade usa client.name)
     * - "totalValue" → "totalValue" (mesmo nome)
     * 
     * @param pageable Pageable com parâmetros de ordenação da requisição HTTP
     * @return Pageable convertido com nomes de propriedades da entidade para ordenação no banco
     */
    private Pageable convertPageable(Pageable pageable) {
        // Mapeamento: chave = nome usado na URL sort=nameClient, valor = entidade
        var mapper = Map.of(
            "code", "code",                           
            "totalValue", "totalValue",
            "subtotal", "subtotal",
            "feeShipping", "feeShipping",
            "nameRestaurant", "restaurant.name",             
            "nameClient", "client.name",                       
            "status", "status",                       
            "createdAt", "createdAt"
        );
        return PageableTranslator.translate(pageable, mapper);
    }
}