package com.gtech.food_api.core.mapper;

import org.springframework.context.annotation.Configuration;
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
        return new ModelMapper();
    }

}
