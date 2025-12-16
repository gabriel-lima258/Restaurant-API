package com.gtech.food_api.api.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtech.food_api.api.assembler.RestaurantDTOAssembler;
import com.gtech.food_api.api.disassembler.RestaurantInputDisassembler;
import com.gtech.food_api.api.dto.RestaurantDTO;
import com.gtech.food_api.api.dto.input.RestaurantInput;
import com.gtech.food_api.core.validation.ValidationException;
import com.gtech.food_api.domain.model.Restaurant;
import com.gtech.food_api.domain.service.RestaurantService;
import com.gtech.food_api.domain.service.exceptions.BusinessException;
import com.gtech.food_api.domain.service.exceptions.CityNotFoundException;
import com.gtech.food_api.domain.service.exceptions.KitchenNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private SmartValidator validator;
    
    @Autowired
    private RestaurantDTOAssembler restaurantDTOAssembler;

    @Autowired
    private RestaurantInputDisassembler restaurantInputDisassembler;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> listAll(){
        List<Restaurant> result = restaurantService.listAll();
        List<RestaurantDTO> dtoList = restaurantDTOAssembler.toCollectionDTO(result);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> findById(@PathVariable Long id) {
        Restaurant entity = restaurantService.findOrFail(id);
        RestaurantDTO dto = restaurantDTOAssembler.copyToDTO(entity);
        return ResponseEntity.ok().body(dto);
    }

    // @Validated(Groups.RegisterRestaurant.class) serve para especificar qual grupo de validação deve ser aplicado
    // @Valid por padrao é o grupo default, o que pode ser um problema se a classe tiver outros grupos de validação
    @PostMapping
    public ResponseEntity<RestaurantDTO> save(@RequestBody @Valid RestaurantInput restaurantInput) {
        try {
            // recebe dto input e converte para entity
            Restaurant restaurant = restaurantInputDisassembler.copyToEntity(restaurantInput);
            // salva a entity
            Restaurant entity = restaurantService.save(restaurant);
            // cria o uri para o novo restaurante
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(entity.getId()).toUri();   
            // converte a entity salva para dto
            RestaurantDTO dto = restaurantDTOAssembler.copyToDTO(entity);

            return ResponseEntity.created(uri).body(dto);
        } catch (KitchenNotFoundException | CityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @RequestBody @Valid RestaurantInput restaurantInput) {
        try {
            // busca o restaurante existente
            Restaurant restaurant = restaurantService.findOrFail(id);
            // recebe dto input e converte para entity e atualiza a entity existente    
            restaurantInputDisassembler.copyToDomainObject(restaurantInput, restaurant);
            // salva a entity atualizada
            restaurantService.save(restaurant);
            // converte a entity atualizada para dto e retorna
            RestaurantDTO dto = restaurantDTOAssembler.copyToDTO(restaurant);
            return ResponseEntity.ok().body(dto);
        } catch (KitchenNotFoundException | CityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }

     // idempotente, pois pode ser chamado quantas vezes quiser, o resultado será o mesmo
     @PutMapping("/{id}/active")
     public ResponseEntity<Void> activate(@PathVariable Long id) {
         restaurantService.activate(id);    
         return ResponseEntity.noContent().build();
     }
 
     // idempotente, por coerencia, o delete é usado para desativar o restaurante, pois remove um recurso
     @DeleteMapping("/{id}/deactive")
     public ResponseEntity<Void> deactivate(@PathVariable Long id) {
         restaurantService.deactivate(id);
         return ResponseEntity.noContent().build();
     }

     @PutMapping("/{id}/opening")
     public ResponseEntity<Void> openRestaurant(@PathVariable Long id) {
         restaurantService.open(id);
         return ResponseEntity.noContent().build();
     }

     @PutMapping("/{id}/closing")
     public ResponseEntity<Void> closeRestaurant(@PathVariable Long id) {
         restaurantService.close(id);
         return ResponseEntity.noContent().build();
     }

    /**
     * Atualização parcial de restaurante (PATCH).
     * Atualiza apenas os campos enviados no Map, mantendo os demais inalterados.
     * 
     * @param id ID do restaurante
     * @param fields Campos a atualizar (ex: {"name": "Novo Nome", "freightFee": 5.50})
     * @param request HttpServletRequest para tratamento de erros
     * @return ResponseEntity 200 OK
     */
    @PatchMapping("/{id}")
    public ResponseEntity<RestaurantDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> fields, HttpServletRequest request) {
        // Busca restaurante existente
        Restaurant currentRestaurant = restaurantService.findOrFail(id);
        
        // Mescla campos do Map com o objeto existente
        merge(fields, currentRestaurant, request);

        // Valida o objeto após merge
        validate(currentRestaurant, "restaurant");
        
        // Salva alterações
        restaurantService.save(currentRestaurant);

        RestaurantDTO dto = restaurantDTOAssembler.copyToDTO(currentRestaurant);

        return ResponseEntity.ok().body(dto);
    }

    /**
     * Valida o objeto Restaurant usando SmartValidator.
     * Lança ValidationException se houver erros de validação.
     */
    private void validate(Restaurant restaurant, String objectName) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurant, objectName);
        validator.validate(restaurant, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
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

            // Atualiza cada campo no objeto destino usando Reflection
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
