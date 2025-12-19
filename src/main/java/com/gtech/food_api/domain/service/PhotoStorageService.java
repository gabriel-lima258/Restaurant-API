package com.gtech.food_api.domain.service;

import java.io.InputStream;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

/*
 * Interface para armazenar fotos em diferentes sistemas de armazenamento
 * Serve como contrato para implementações de diferentes sistemas de armazenamento
 * Exemplo: Amazon S3, Google Cloud Storage, local, etc.
 */
public interface PhotoStorageService {

    InputStream recoverFile(String fileName);
    
    void storeFile(NewPhoto newPhoto);

    void removeFile(String fileName);

    // por que usar default? porque é uma implementação padrão, e pode ser sobrescrita caso necessario
    default void replaceFile(String oldFileName, NewPhoto newPhoto) {
        // armazenamos a nova foto
        this.storeFile(newPhoto);

        // se existe uma foto existente, removemos a foto existente para nao ficar duplicada
        if (oldFileName != null) {
            this.removeFile(oldFileName);
        }
    }

    default String generateNewFileName(String fileName) {
        return UUID.randomUUID().toString() + "_" + fileName;
    }

    @Builder
    @Getter
    class NewPhoto {
        String fileName;
        InputStream inputStream; // fluxo de entrada de dados
    }
}
