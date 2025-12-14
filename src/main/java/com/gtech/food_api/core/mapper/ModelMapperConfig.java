package com.gtech.food_api.core.mapper;

import org.springframework.context.annotation.Configuration;

import com.gtech.food_api.api.dto.AddressDTO;
import com.gtech.food_api.domain.model.Address;

import org.springframework.context.annotation.Bean;

import org.modelmapper.ModelMapper;

/**
 * Configuração do ModelMapper
 * 
 * O que é ModelMapper?
 * - ModelMapper é uma biblioteca que facilita a conversão de entidades para DTOs e vice-versa
 * - ModelMapper é uma biblioteca que facilita a conversão de DTOs para entidades e vice-versa
 * 
 * Como funciona:
 * - @Configuration: Spring detecta e registra automaticamente esta configuração
 * - @Bean: Spring cria uma instância do ModelMapper e a injeta em outras classes
 * - modelMapper: cria uma instância do ModelMapper
 * 
 * Por que fazer isso?
 * - Usamos Bean para injetar o ModelMapper em outras classes
 */
@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        var modelMapper = new ModelMapper();

        /**
         * Mapear o endereço para o endereço DTO
         * - src: endereço
         * - dest: endereço DTO
         * - value: nome do estado
         * - dest.getCity().setState((String) value): setar o nome do estado no endereço DTO
         * 
         * Para que isso?
         * - Para que o endereço DTO tenha somente o nome do estado, sem a necessidade de buscar o estado completo
         */
        var addressToAddressDTO = modelMapper.createTypeMap(Address.class, AddressDTO.class);
        addressToAddressDTO.addMapping(
            src -> src.getCity().getState().getName(),
            (dest, value) -> dest.getCity().setState((String) value));

        return modelMapper;
    }

}
