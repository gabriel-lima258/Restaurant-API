package com.gtech.food_api.infra.repository.specification;

import com.gtech.food_api.domain.model.Restaurant;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Factory de specification para Restaurant com frete grátis
 * 
 * Como funciona:
 * - withFreeFee: cria um predicate de free shipping
 * - withSimiliarName: cria um predicate de similar name
 * 
 * Exemplo de uso:
 * List<Restaurant> restaurants = restaurantRepository.findAll(RestaurantFreeFeeSpec.withFreeFee());
 */
public class RestaurantFreeFeeSpec {

    /*
    * RestaurantFreeFeeSpec: classe de specification para Restaurant com frete grátis
    * o que é um predicate? é uma expressão booleana que retorna true ou false, exemplo: root.get("shippingFee").equal(BigDecimal.ZERO)
    * @return Specification<Restaurant>
    */
    public static Specification<Restaurant> withFreeFee() {
        return ((root, query, builder) ->
                builder.equal(root.get("shippingFee"), BigDecimal.ZERO));
    }

    /*
    * withSimiliarName: criando um predicate de similar name
    * o que é um predicate? é uma expressão booleana que retorna true ou false, exemplo: root.get("name").like("%" + name + "%")
    * @param name
    * @return Specification<Restaurant>
    */
    public static Specification<Restaurant> withSimiliarName(String name) {
        return (((root, query, builder) ->
                builder.like(root.get("name"), "%" + name + "%")));
    }
}
