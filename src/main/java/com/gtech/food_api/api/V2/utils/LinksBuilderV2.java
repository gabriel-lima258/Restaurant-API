package com.gtech.food_api.api.V2.utils;

import com.gtech.food_api.api.V2.controller.*;
import org.springframework.hateoas.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

@Component
public class LinksBuilderV2 {

    // Cria variáveis de template para parâmetros de paginação e ordenação
    // Essas variáveis permitem que o link seja um template URI que aceita parâmetros opcionais
    // Exemplo: /orders{?page,size,sort} ao invés de apenas /orders
    private static final TemplateVariables PAGINATION_VARIABLES = new TemplateVariables(
        new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
        new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM),
        new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM)
    );

    public Link linkToOrders(String rel) {
        TemplateVariables filterVariables = new TemplateVariables(
            new TemplateVariable("clientId", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("restaurantId", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("creationDateStart", TemplateVariable.VariableType.REQUEST_PARAM),
            new TemplateVariable("creationDateEnd", TemplateVariable.VariableType.REQUEST_PARAM)
        );

        // Gera a URI base do OrderController (ex: "http://localhost:8080/orders")
        // linkTo() cria um link para o controller, e toUri() converte para String
        String uriTemplate = linkTo(OrderControllerV2.class).toUri().toString();

        // Cria um Link HATEOAS com template URI que aceita os parâmetros de paginação
        // UriTemplate.of() combina a URI base com as variáveis de template
        // O link resultante será algo como: /orders{?page,size,sort}
        // Isso permite que o cliente da API construa URLs de paginação facilmente
        return Link.of(UriTemplate.of(uriTemplate, PAGINATION_VARIABLES.concat(filterVariables)).toString(), rel);
    }

    public Link linkToReports(String rel) {
        return linkTo(ReportControllerV2.class).withRel(rel);
    }
    
    public Link linkToReportsVendasDiarias(String rel) {
        TemplateVariables filtroVariables = new TemplateVariables(
                new TemplateVariable("restaurantId", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateStart", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("creationDateEnd", TemplateVariable.VariableType.REQUEST_PARAM),
                new TemplateVariable("timeOffset", TemplateVariable.VariableType.REQUEST_PARAM));
        
        String pedidosUrl = linkTo(methodOn(ReportControllerV2.class)
                .queryDailySelling(null, null)).toUri().toString();
        
        return Link.of(UriTemplate.of(pedidosUrl, filtroVariables).toString(), rel);
    }       

    /**
     * "_links": {
            "product": { -> rel = "product"
                "href": "http://localhost:8080/restaurants/3/products/5"
            }
        }
    */
    public Link linkToKitchen(Long kitchenId, String rel) {
        return linkTo(methodOn(KitchenControllerV2.class).findById(kitchenId)).withRel(rel);
    }

    /**
     * "self": {
            "href": "http://localhost:8080/kitchens/1"
        },
    */
    public Link linkToKitchen(Long kitchenId) {
        return linkToKitchen(kitchenId, IanaLinkRelations.SELF.value());
    }

    /**
     * Exemplo de uso:
     * "kitchens": {
            "href": "http://localhost:8080/kitchens"
        }
    */
    public Link linkToKitchens() {
        return linkTo(KitchenControllerV2.class).withRel("kitchens");
    }

    public Link linkToRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantControllerV2.class).findById(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurant(Long restaurantId) {
        return linkToRestaurant(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurants() {
        return linkTo(RestaurantControllerV2.class).withRel("restaurants");
    }

    public Link linkToRestaurantResponsible(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantResponsibleControllerV2.class).listAll(restaurantId)).withRel(rel);
    }

    public Link linkToAddResponsibleRestaurant(Long restaurantId) {
        return linkTo(methodOn(RestaurantResponsibleControllerV2.class).addResponsible(restaurantId, null)).withRel("add-responsible");
    }

    public Link linkToRemoveResponsibleRestaurant(Long restaurantId, Long userId) {
        return linkTo(methodOn(RestaurantResponsibleControllerV2.class).removeResponsible(restaurantId, userId)).withRel("remove-responsible");
    }

    public Link linkToRestaurantResponsible(Long restaurantId) {
        return linkTo(methodOn(RestaurantResponsibleControllerV2.class).listAll(restaurantId)).withSelfRel();
    }

    public Link linkToRestaurantPaymentMethods(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodControllerV2.class).listAll(restaurantId)).withRel(rel);
    }

    public Link linkToDesassociatePaymentMethodRestaurant(Long restaurantId, Long paymentMethodId) {
        return linkTo(methodOn(RestaurantPaymentMethodControllerV2.class).disassociatePaymentMethod(restaurantId, paymentMethodId)).withRel("desassociate-payment-method");
    }

    public Link linkToAssociatePaymentMethodRestaurant(Long restaurantId) {
        return linkTo(methodOn(RestaurantPaymentMethodControllerV2.class).associatePaymentMethod(restaurantId, null)).withRel("associate-payment-method");
    }

    public Link linkToOpenRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantControllerV2.class).openRestaurant(restaurantId)).withRel(rel);
    }

    public Link linkToCloseRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantControllerV2.class).closeRestaurant(restaurantId)).withRel(rel);
    }

    public Link linkToActivateRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantControllerV2.class).activate(restaurantId)).withRel(rel);
    }

    public Link linkToDeactivateRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantControllerV2.class).deactivate(restaurantId)).withRel(rel);
    }

    public Link linkToUser(Long userId, String rel) {
        return linkTo(methodOn(UserControllerV2.class).findById(userId)).withRel(rel);
    }

    public Link linkToUser(Long userId) {
        return linkToUser(userId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUsers() {
        return linkTo(UserControllerV2.class).withRel("users");
    }

    public Link linkToGroups() {
        return linkTo(GroupControllerV2.class).withRel("groups");
    }

    public Link linkToUserGroups(Long userId) {
        return linkTo(methodOn(UserGroupControllerV2.class).listAll(userId)).withRel("groups-users");
    }

    public Link linkToPermissions() {
        return linkTo(PermissionControllerV2.class).withRel("permissions");
    }

    public Link linkToGroupPermissions(Long groupId) {
        return linkTo(methodOn(GroupPermissionControllerV2.class).listAll(groupId)).withRel("permissions-group");
    }

    public Link linkToAssociatePermissionGroup(Long groupId) {
        return linkTo(methodOn(GroupPermissionControllerV2.class).addPermission(groupId, null)).withRel("associate-permission");
    }

    public Link linkToRemovePermissionGroup(Long groupId, Long permissionId) {
        return linkTo(methodOn(GroupPermissionControllerV2.class).removePermission(groupId, permissionId)).withRel("remove-permission");
    }

    public Link linkToAssociatePermissionUser(Long userId) {
        return linkTo(methodOn(UserGroupControllerV2.class).associateGroup(userId, null)).withRel("associate-permission");
    }

    public Link linkToRemovePermissionUser(Long userId, Long permissionId) {
        return linkTo(methodOn(UserGroupControllerV2.class).disassociateGroup(userId, permissionId)).withRel("remove-permission");
    }

    public Link linkToPaymentMethod(Long paymentMethodId, String rel) {
        return linkTo(methodOn(PaymentMethodControllerV2.class).findById(paymentMethodId)).withRel(rel);
    }

    public Link linkToPaymentMethod(Long paymentMethodId) {
        return linkToPaymentMethod(paymentMethodId, IanaLinkRelations.SELF.value());
    }

    public Link linkToPaymentMethods() {
        return linkTo(PaymentMethodControllerV2.class).withRel("payment-methods");
    }

    public Link linkToCity(Long cityId, String rel) {
        return linkTo(methodOn(CityControllerV2.class).findById(cityId)).withRel(rel);
    }

    public Link linkToCity(Long cityId) {
        return linkToCity(cityId, IanaLinkRelations.SELF.value());
    }

    public Link linkToCities() {
        return linkTo(CityControllerV2.class).withRel("cities");
    }

    public Link linkToState(Long stateId, String rel) {
        return linkTo(methodOn(StateControllerV2.class).findById(stateId)).withRel(rel);
    }

    public Link linkToState(Long stateId) {
        return linkToState(stateId, IanaLinkRelations.SELF.value());
    }

    public Link linkToStates() {
        return linkTo(StateControllerV2.class).withRel("states");
    }

    public Link linkToProduct(Long productId, Long restaurantId, String rel) {
        return linkTo(methodOn(ProductControllerV2.class).findById(productId, restaurantId)).withRel(rel);
    }

    public Link linkToProduct(Long productId, Long restaurantId) {
        return linkToProduct(productId, restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToProducts(Long restaurantId, String rel) {
        return linkTo(methodOn(ProductControllerV2.class).listAll(restaurantId, null)).withRel(rel);
    }

    public Link linkToPhotoProduct(Long productId, Long restaurantId, String rel) {
        return linkTo(methodOn(ProductFileControllerV2.class).getPhoto(productId, restaurantId)).withRel(rel);
    }

    public Link linkToPhotoProduct(Long productId, Long restaurantId) {
        return linkToPhotoProduct(productId, restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToConfimOrder(String orderCode, String rel) {
        return linkTo(methodOn(StatusOrderControllerV2.class).confirm(orderCode)).withRel(rel);
    }

    public Link linkToDeliverOrder(String orderCode, String rel) {
        return linkTo(methodOn(StatusOrderControllerV2.class).deliver(orderCode)).withRel(rel);
    }

    public Link linkToCancelOrder(String orderCode, String rel) {
        return linkTo(methodOn(StatusOrderControllerV2.class).cancel(orderCode)).withRel(rel);
    }
    
}
