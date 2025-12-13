package com.gtech.food_api.infra.repository.specification;

import com.gtech.food_api.domain.repository.CustomJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;

/**
 * Implementação do CustomJpaRepository
 * 
 * Como funciona:
 * - @extends SimpleJpaRepository: extende a classe SimpleJpaRepository
 * - @implements CustomJpaRepository: implementa a interface CustomJpaRepository
 * - searchFirst: busca o primeiro resultado da consulta
 */
public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements CustomJpaRepository<T, ID> {

    private EntityManager entityManager;

    public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    // classe generica de busca de classes
    @Override
    public Optional<T> searchFirst() {
        var jpql = "FROM " + getDomainClass().getName();

        T entity = entityManager.createQuery(jpql, getDomainClass()) // entidade passada
                .setMaxResults(1) // limita a busca para 1 resultado
                .getSingleResult();

        return Optional.ofNullable(entity);
    }
}
