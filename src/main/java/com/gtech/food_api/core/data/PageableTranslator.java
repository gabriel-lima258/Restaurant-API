package com.gtech.food_api.core.data;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;
import java.util.stream.Collectors;

public class PageableTranslator {
    
    public static Pageable translate(Pageable pageable, Map<String, String> fieldsMapping) {
        var orders = pageable.getSort().stream()
        .filter(order -> fieldsMapping.containsKey(order.getProperty())) // filtra apenas mappings existentes não nulos
        .map(order -> 
            new Sort.Order(order.getDirection(), // direção da ordenação (asc ou desc)
            fieldsMapping.get(order.getProperty())) // campo a ser ordenado, ex: client.name -> nameClient
        ).collect(Collectors.toList()); // nomes de propriedades ja convertidos

        // instancia um Pageable, com o numero da pagina, o tamanho da pagina e a lista de ordenações
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
    }
}
