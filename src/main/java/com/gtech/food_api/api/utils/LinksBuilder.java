package com.gtech.food_api.api.utils;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;

import com.gtech.food_api.api.controller.CityController;
import com.gtech.food_api.api.controller.KitchenController;
import com.gtech.food_api.api.controller.OrderController;
import com.gtech.food_api.api.controller.PaymentMethodController;
import com.gtech.food_api.api.controller.ProductController;
import com.gtech.food_api.api.controller.RestaurantController;
import com.gtech.food_api.api.controller.RestaurantPaymentMethodController;
import com.gtech.food_api.api.controller.RestaurantResponsibleController;
import com.gtech.food_api.api.controller.StateController;
import com.gtech.food_api.api.controller.StatusOrderController;
import com.gtech.food_api.api.controller.UserController;
import com.gtech.food_api.api.controller.UserGroupController;

@Component
public class LinksBuilder {

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
        String uriTemplate = linkTo(OrderController.class).toUri().toString();

        // Cria um Link HATEOAS com template URI que aceita os parâmetros de paginação
        // UriTemplate.of() combina a URI base com as variáveis de template
        // O link resultante será algo como: /orders{?page,size,sort}
        // Isso permite que o cliente da API construa URLs de paginação facilmente
        return Link.of(UriTemplate.of(uriTemplate, PAGINATION_VARIABLES.concat(filterVariables)).toString(), rel);
    }

    /**
     * "_links": {
            "product": { -> rel = "product"
                "href": "http://localhost:8080/restaurants/3/products/5"
            }
        }
    */
    public Link linkToKitchen(Long kitchenId, String rel) {
        return linkTo(methodOn(KitchenController.class).findById(kitchenId)).withRel(rel);
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
        return linkTo(KitchenController.class).withRel("kitchens");
    }

    public Link linkToRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).findById(restaurantId)).withRel(rel);
    }

    public Link linkToRestaurant(Long restaurantId) {
        return linkToRestaurant(restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToRestaurants() {
        return linkTo(RestaurantController.class).withRel("restaurants");
    }

    public Link linkToRestaurantResponsible(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantResponsibleController.class).listAll(restaurantId)).withRel(rel);
    }

    public Link linkToAddResponsibleRestaurant(Long restaurantId) {
        return linkTo(methodOn(RestaurantResponsibleController.class).addResponsible(restaurantId, null)).withRel("add-responsible");
    }

    public Link linkToRemoveResponsibleRestaurant(Long restaurantId, Long userId) {
        return linkTo(methodOn(RestaurantResponsibleController.class).removeResponsible(restaurantId, userId)).withRel("remove-responsible");
    }

    public Link linkToRestaurantResponsible(Long restaurantId) {
        return linkTo(methodOn(RestaurantResponsibleController.class).listAll(restaurantId)).withSelfRel();
    }

    public Link linkToRestaurantPaymentMethods(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class).listAll(restaurantId)).withRel(rel);
    }

    public Link linkToDesassociatePaymentMethodRestaurant(Long restaurantId, Long paymentMethodId) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class).disassociatePaymentMethod(restaurantId, paymentMethodId)).withRel("desassociate-payment-method");
    }

    public Link linkToAssociatePaymentMethodRestaurant(Long restaurantId) {
        return linkTo(methodOn(RestaurantPaymentMethodController.class).associatePaymentMethod(restaurantId, null)).withRel("associate-payment-method");
    }

    public Link linkToOpenRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).openRestaurant(restaurantId)).withRel(rel);
    }

    public Link linkToCloseRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).closeRestaurant(restaurantId)).withRel(rel);
    }

    public Link linkToActivateRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).activate(restaurantId)).withRel(rel);
    }

    public Link linkToDeactivateRestaurant(Long restaurantId, String rel) {
        return linkTo(methodOn(RestaurantController.class).deactivate(restaurantId)).withRel(rel);
    }

    public Link linkToUser(Long userId, String rel) {
        return linkTo(methodOn(UserController.class).findById(userId)).withRel(rel);
    }

    public Link linkToUser(Long userId) {
        return linkToUser(userId, IanaLinkRelations.SELF.value());
    }

    public Link linkToUsers() {
        return linkTo(UserController.class).withRel("users");
    }

    public Link linkToUserGroups(Long userId) {
        return linkTo(methodOn(UserGroupController.class).listAll(userId)).withRel("groups-users");
    }

    public Link linkToPaymentMethod(Long paymentMethodId, String rel) {
        return linkTo(methodOn(PaymentMethodController.class).findById(paymentMethodId)).withRel(rel);
    }

    public Link linkToPaymentMethod(Long paymentMethodId) {
        return linkToPaymentMethod(paymentMethodId, IanaLinkRelations.SELF.value());
    }

    public Link linkToPaymentMethods() {
        return linkTo(PaymentMethodController.class).withRel("payment-methods");
    }

    public Link linkToCity(Long cityId, String rel) {
        return linkTo(methodOn(CityController.class).findById(cityId)).withRel(rel);
    }

    public Link linkToCity(Long cityId) {
        return linkToCity(cityId, IanaLinkRelations.SELF.value());
    }

    public Link linkToCities() {
        return linkTo(CityController.class).withRel("cities");
    }

    public Link linkToState(Long stateId, String rel) {
        return linkTo(methodOn(StateController.class).findById(stateId)).withRel(rel);
    }

    public Link linkToState(Long stateId) {
        return linkToState(stateId, IanaLinkRelations.SELF.value());
    }

    public Link linkToStates() {
        return linkTo(StateController.class).withRel("states");
    }

    public Link linkToProduct(Long productId, Long restaurantId, String rel) {
        return linkTo(methodOn(ProductController.class).findById(productId, restaurantId)).withRel(rel);
    }

    public Link linkToProduct(Long productId, Long restaurantId) {
        return linkToProduct(productId, restaurantId, IanaLinkRelations.SELF.value());
    }

    public Link linkToProducts(Long restaurantId, String rel) {
        return linkTo(methodOn(ProductController.class).listAll(restaurantId, null)).withRel(rel);
    }

    public Link linkToConfimOrder(String orderCode, String rel) {
        return linkTo(methodOn(StatusOrderController.class).confirm(orderCode)).withRel(rel);
    }

    public Link linkToDeliverOrder(String orderCode, String rel) {
        return linkTo(methodOn(StatusOrderController.class).deliver(orderCode)).withRel(rel);
    }

    public Link linkToCancelOrder(String orderCode, String rel) {
        return linkTo(methodOn(StatusOrderController.class).cancel(orderCode)).withRel(rel);
    }
    
}
