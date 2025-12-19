package com.gtech.food_api.infra.repository;

import com.gtech.food_api.domain.model.PhotoProduct;
import com.gtech.food_api.domain.repository.ProductRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

/**
 * Implementação da interface ProductRepositoryQueries
 * 
 * Como funciona:
 * - @Repository: Spring detecta e registra automaticamente esta implementação
 * - save: salva Foto do Produto
 * 
 * Exemplo de uso:
 * PhotoProduct photoProduct = productRepositoryQueries.save(photoProduct);
 * 
 * Vantagens:
 * - Usamos o entityManager para salvar a Foto do Produto
 */
@Repository
public class ProductRepositoryImpl implements ProductRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PhotoProduct savePhoto(PhotoProduct photoProduct) {
        return entityManager.merge(photoProduct);
    }
  
}
