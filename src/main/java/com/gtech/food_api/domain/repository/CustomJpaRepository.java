package com.gtech.food_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

// repositorio customizado para criar metodos genericos nossos no jpa e usar em outros repositorios
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> searchFirst();

    // desanexa a entidade do contexto de persistencia, para que seja possivel atualizar o objeto sem precisar salvar
    void detach(T entity);
}
