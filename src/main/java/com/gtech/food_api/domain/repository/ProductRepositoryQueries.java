package com.gtech.food_api.domain.repository;

import com.gtech.food_api.domain.model.PhotoProduct;

/*
 * - save: salva Foto do Produto
 * 
 * Exemplo de uso:
 * PhotoProduct photoProduct = productRepositoryQueries.save(photoProduct);
 * 
 * Classe de implementação: ProductRepositoryImpl
 */
public interface ProductRepositoryQueries {

    PhotoProduct savePhoto(PhotoProduct photoProduct);
    void deletePhoto(PhotoProduct photoProduct);
}
