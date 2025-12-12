package com.gtech.food_api.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gtech.food_api.domain.model.Address;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.model.PaymentMethod;
import com.gtech.food_api.domain.model.Product;
import com.gtech.food_api.domain.model.Order;

/**
 * Jackson Mixin: Classe que isola as configurações de serialização JSON da entidade Restaurant.
 * 
 * POR QUE ISOLAR?
 * 
 * 1. SEPARAÇÃO DE RESPONSABILIDADES:
 *    - Restaurant (domain) = regras de negócio, validações, persistência (JPA)
 *    - RestaurantMixin (api) = apenas configurações de serialização JSON
 *    - Mantém a camada de domínio limpa, sem acoplamento com detalhes da API
 * 
 * 2. FLEXIBILIDADE:
 *    - Permite diferentes representações JSON para a mesma entidade
 *    - Ex: RestaurantSummaryMixin (apenas id e name) vs RestaurantFullMixin (todos os campos)
 *    - Pode criar múltiplos mixins para diferentes endpoints/contextos
 * 
 * 3. REUTILIZAÇÃO:
 *    - A entidade Restaurant pode ser usada em outros contextos (JPA, validação, etc)
 *    - Sem "poluir" a entidade com anotações específicas de serialização
 * 
 * 4. MANUTENIBILIDADE:
 *    - Mudanças na API não afetam a entidade de domínio
 *    - Facilita testes unitários da entidade (sem dependências do Jackson)
 *    - Facilita migração para outras bibliotecas de serialização no futuro
 * 
 * COMO FUNCIONA:
 * - O Jackson "mistura" as anotações desta classe com a entidade Restaurant
 * - Quando Restaurant é serializado, o Jackson aplica as regras definidas aqui
 * - A entidade Restaurant permanece sem anotações @JsonIgnore, @JsonIgnoreProperties, etc
 * 
 * REGISTRO:
 * - Este mixin deve ser registrado no ObjectMapper via JacksonMixinModule
 * - Ex: objectMapper.addMixIn(Restaurant.class, RestaurantMixin.class)
 */
public abstract class RestaurantMixin {
    // Campos internos: não devem ser expostos na API REST
    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    // Endereço: pode ser exposto em endpoints específicos, mas não no resumo
    @JsonIgnore
    private Address address;

    // Kitchen: ignora propriedades da Kitchen no JSON, mas permite acessar getters
    // Exemplo: permite kitchen.getName() mas não serializa outros campos da Kitchen
    @JsonIgnoreProperties(value = "name", allowGetters = true)
    private Kitchen kitchen;

    // Relacionamentos: ignorados para evitar:
    // - Loops infinitos na serialização (Restaurant -> PaymentMethod -> Restaurant...)
    // - Respostas JSON muito grandes e lentas
    // - Exposição de dados sensíveis ou desnecessários
    @JsonIgnore
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @JsonIgnore
    List<Product> products = new ArrayList<>();

    @JsonIgnore
    private List<Order> orders = new ArrayList<>();
}
