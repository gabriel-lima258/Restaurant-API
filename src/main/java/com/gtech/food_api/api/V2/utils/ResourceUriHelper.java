package com.gtech.food_api.api.V1.utils;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.experimental.UtilityClass;

/**
 * Esta classe fornece métodos auxiliares para gerar URIs que seguem o padrão REST,
 * onde após criar um recurso, retornamos a URI completa do recurso criado no header
 * da resposta HTTP (Location header). Padrão do HATEOAS.
 * 
 * O uso desta classe é especialmente útil em endpoints POST que criam novos recursos,
 * seguindo a convenção HTTP 201 Created, onde o header Location deve conter a URI
 * do recurso recém-criado.
 * 
 * Este método pega a URI da requisição atual (ex: http://localhost:8080/restaurants)
 * e adiciona o ID do recurso criado, resultando em uma URI completa
 * (ex: http://localhost:8080/restaurants/123).
 * 
 * Como funciona:
 * 1. Obtém a URI base da requisição atual usando ServletUriComponentsBuilder
 * 2. Adiciona o path "/{id}" à URI base
 * 3. Substitui o placeholder {id} pelo ID do recurso criado
 * 4. Retorna a URI completa como objeto URI
 * 
 * Exemplo:
 * - Requisição: POST http://localhost:8080/restaurants
 * - resourceId: 42
 * - URI retornada: http://localhost:8080/restaurants/42
 * 
 * @UtilityClass: Anotação do Lombok que torna a classe final, adiciona um construtor
 * privado e torna todos os métodos estáticos, garantindo que não possa ser instanciada.
 */
@UtilityClass
public class ResourceUriHelper {
    
    public static URI addUriInResponseHeader(Object resourceId) {
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
        .path("/{id}")
        .buildAndExpand(resourceId)
        .toUri();

        return uri;
    }
}
