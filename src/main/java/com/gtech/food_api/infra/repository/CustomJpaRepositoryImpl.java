package com.gtech.food_api.infra.repository;

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
 * - detach: desanexa a entidade do contexto de persistencia, para que seja possivel atualizar o objeto sem precisar salvar. por que isso? porque o objeto ja esta no contexto de persistencia e o jpa nao consegue detectar as alterações.
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

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }
}
