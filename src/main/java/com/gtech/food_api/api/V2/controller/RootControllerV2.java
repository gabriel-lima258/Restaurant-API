package com.gtech.food_api.api.V2.controller;

import com.gtech.food_api.api.V2.utils.LinksBuilderV2;
import com.gtech.food_api.core.security.resource.validations.UsersJwtSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller que implementa o Entry Point da API seguindo o padrão HATEOAS.
 * 
 * O QUE É UM ENTRY POINT?
 * - É um ponto de entrada único da API que fornece links para todos os recursos principais
 * - Permite que o cliente descubra dinamicamente quais recursos estão disponíveis
 * - Segue o princípio REST de "hipermídia como motor do estado da aplicação" (HATEOAS)
 * 
 * COMO FUNCIONA:
 * - GET / retorna um JSON com links para todos os recursos principais da API
 * - Cada link permite navegar para diferentes partes da API sem precisar conhecer as URLs antecipadamente
 * - O cliente pode descobrir a estrutura da API fazendo uma requisição GET na raiz
 * 
 * EXEMPLO DE RESPOSTA:
 * ```json
 * {
 *   "_links": {
 *     "kitchens": { "href": "http://localhost:8080/kitchens" },
 *     "restaurants": { "href": "http://localhost:8080/restaurants" },
 *     "paymentMethods": { "href": "http://localhost:8080/payment-methods" },
 *     "permissions": { "href": "http://localhost:8080/permissions" },
 *     "groups": { "href": "http://localhost:8080/groups" },
 *     "users": { "href": "http://localhost:8080/users" },
 *     "cities": { "href": "http://localhost:8080/cities" },
 *     "states": { "href": "http://localhost:8080/states" },
 *     "orders": { "href": "http://localhost:8080/orders{?page,size,sort,...}" }
 *   }
 * }
 * ```
 * 
 * BENEFÍCIOS:
 * - Auto-descrição: A API se descreve automaticamente
 * - Desacoplamento: Cliente não precisa conhecer URLs hardcoded
 * - Evolução: Novos recursos podem ser adicionados sem quebrar clientes antigos
 * - Navegação: Facilita a descoberta e navegação entre recursos relacionados
 * 
 * @RequestMapping sem path: Mapeia para a raiz da aplicação (GET /)
 * @RequestMapping produces: Garante que sempre retorna JSON (MediaType.APPLICATION_JSON_VALUE)
 */
@RestController
@RequestMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootControllerV2 {

    @Autowired
    private LinksBuilderV2 linksBuilder;

    @Autowired
    private UsersJwtSecurity usersJwtSecurity;

    @GetMapping
    public RootEntryPointModel root() {
        var rootEntryPointModel = new RootEntryPointModel();
        if (usersJwtSecurity.canViewKitchens()) {
            rootEntryPointModel.add(linksBuilder.linkToKitchens());
        }

        if (usersJwtSecurity.canViewRestaurants()) {
            rootEntryPointModel.add(linksBuilder.linkToRestaurants());
        }

        if (usersJwtSecurity.canViewPayments()) {
            rootEntryPointModel.add(linksBuilder.linkToPaymentMethods());
        }

        if (usersJwtSecurity.canViewUsersGroupsPermissions()) {
            rootEntryPointModel.add(linksBuilder.linkToPermissions());
            rootEntryPointModel.add(linksBuilder.linkToGroups());
            rootEntryPointModel.add(linksBuilder.linkToUsers());
        }

        if (usersJwtSecurity.canViewCities()) {
            rootEntryPointModel.add(linksBuilder.linkToCities());
        }

        if (usersJwtSecurity.canViewStates()) {
            rootEntryPointModel.add(linksBuilder.linkToStates());
        }

        if (usersJwtSecurity.canViewOrders()) {
            rootEntryPointModel.add(linksBuilder.linkToOrders("orders"));
        }

        if (usersJwtSecurity.canGenerateReports()) {
            rootEntryPointModel.add(linksBuilder.linkToReports("reports"));
        }

        return rootEntryPointModel;
    }

    /**
     * Classe interna que representa o modelo de resposta do Entry Point.
     * 
     * Esta classe estende RepresentationModel para suportar links HATEOAS.
     * É uma classe estática privada porque:
     * - É usada apenas dentro do RootController
     * - Não precisa de estado adicional além dos links herdados
     * - Mantém o código organizado e encapsulado
     * 
     * RepresentationModel fornece a estrutura base para adicionar links HATEOAS,
     * permitindo que o Spring HATEOAS serialize automaticamente os links no JSON.
     */
    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {
    }
}
