package com.gtech.food_api.api.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.KitchenNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<Restaurant>> listAll(){
        List<Restaurant> result = restaurantService.listAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> findById(@PathVariable Long id) {
        Restaurant entity = restaurantService.findOrFail(id);
        return ResponseEntity.ok().body(entity);
    }

    // @Validated(Groups.RegisterRestaurant.class) serve para especificar qual grupo de validação deve ser aplicado
    // @Valid por padrao é o grupo default, o que pode ser um problema se a classe tiver outros grupos de validação
    @PostMapping
    public ResponseEntity<Restaurant> save(@RequestBody @Valid Restaurant restaurant) {
        try {
            Restaurant entity = restaurantService.save(restaurant);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(restaurant.getId()).toUri();
            return ResponseEntity.created(uri).body(entity);
        } catch (KitchenNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> update(@PathVariable Long id, @RequestBody @Valid Restaurant restaurant) {
        Restaurant entity = restaurantService.findOrFail(id);
        try {
            restaurantService.update(id, restaurant);
            return ResponseEntity.ok().body(entity);
        } catch (KitchenNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint para atualização parcial de um restaurante.
     * Permite atualizar apenas campos específicos enviados no corpo da requisição,
     * sem precisar enviar o objeto completo.
     * 
     * @param id Identificador do restaurante a ser atualizado
     * @param fields Map contendo os campos e valores a serem atualizados (ex: {"name": "Novo Nome", "freightFee": 5.50})
     * @param request Objeto HttpServletRequest necessário para tratamento de erros de deserialização
     * @return ResponseEntity com status 200 (OK) após atualização bem-sucedida
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> fields, HttpServletRequest request) {
        // Busca o restaurante existente no banco de dados ou lança exceção se não encontrado
        Restaurant restaurant = restaurantService.findOrFail(id);
        
        // Faz o merge dos campos recebidos com o objeto existente
        // Esta operação atualiza apenas os campos especificados no Map, mantendo os demais inalterados
        merge(fields, restaurant, request);
        
        // Persiste as alterações no banco de dados
        restaurantService.update(id, restaurant);
        
        // Retorna resposta HTTP 200 (OK) indicando sucesso na operação
        return ResponseEntity.ok().build();
    }

    /**
     * Método auxiliar que realiza o merge (fusão) dos campos recebidos no Map
     * com o objeto Restaurant existente.
     * 
     * Utiliza Reflection API do Spring para acessar e modificar campos privados,
     * e Jackson ObjectMapper para converter o Map em um objeto Restaurant temporário,
     * garantindo validação e conversão de tipos adequada.
     * 
     * @param fields Map contendo os campos a serem atualizados (chave = nome do campo, valor = novo valor)
     * @param restaurantDestination Objeto Restaurant existente que será atualizado
     * @param request HttpServletRequest usado para criar mensagens de erro mais informativas
     * @throws HttpMessageNotReadableException se houver erro na conversão dos dados (ex: tipo inválido)
     */
    private void merge(Map<String, Object> fields, Restaurant restaurantDestination, HttpServletRequest request) {
        // Cria um objeto ServletServerHttpRequest a partir do HttpServletRequest
        // Necessário para o tratamento de exceções de deserialização do Spring
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        
        try {
            // Cria um ObjectMapper do Jackson para converter o Map em objeto Restaurant
            // O ObjectMapper é responsável por fazer a conversão de tipos e validações básicas
            ObjectMapper mapper = new ObjectMapper();
            
            // Configura o mapper para falhar quando encontrar propriedades ignoradas
            // (útil quando o Map contém campos que não existem na classe Restaurant)
            mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            
            // Configura o mapper para falhar quando encontrar propriedades desconhecidas
            // (permite que o Map tenha campos extras que serão ignorados na conversão)
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            
            // Converte o Map de campos em um objeto Restaurant temporário
            // Este objeto serve como fonte dos valores já convertidos e validados pelo Jackson
            Restaurant restaurantSource = mapper.convertValue(fields, Restaurant.class);

            // Itera sobre cada campo recebido no Map para atualizar o objeto destino
            fields.forEach((nameField, valueField) -> {
                // Usa Reflection para encontrar o Field (atributo) na classe Restaurant
                // pelo nome do campo recebido no Map
                Field field = ReflectionUtils.findField(Restaurant.class, nameField);
                
                // Torna o campo acessível mesmo que seja privado
                // Necessário para poder ler e escrever valores em campos privados da classe
                field.setAccessible(true);
                
                // Busca o valor já convertido do campo no objeto Restaurant temporário
                // (criado pelo ObjectMapper acima)
                // Isso garante que o valor está no tipo correto (ex: String para Long, etc.)
                Object valueObject = ReflectionUtils.getField(field, restaurantSource);
                
                // Atualiza o campo no objeto Restaurant destino com o valor convertido
                // Apenas os campos especificados no Map serão atualizados, os demais permanecem inalterados
                ReflectionUtils.setField(field, restaurantDestination, valueObject);
            });
        } catch (IllegalArgumentException e) {
            // Captura erros de conversão (ex: tentativa de converter String "abc" para Long)
            // Obtém a causa raiz da exceção para melhor diagnóstico do problema
            Throwable cause = ExceptionUtils.getRootCause(e);
            
            // Lança uma exceção específica do Spring que será tratada pelo GlobalExceptionHandler
            // Esta exceção fornece informações mais detalhadas sobre o erro de deserialização
            throw new HttpMessageNotReadableException(e.getMessage(), cause, inputMessage);
        }
    }
}
