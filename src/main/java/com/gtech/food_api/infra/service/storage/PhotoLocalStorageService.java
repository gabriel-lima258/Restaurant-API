package com.gtech.food_api.infra.service.storage;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.gtech.food_api.core.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.gtech.food_api.domain.service.PhotoStorageService;
import com.gtech.food_api.infra.service.storage.exceptions.StorageException;

//@Service
public class PhotoLocalStorageService implements PhotoStorageService {

    // storageProperties permite escolher qual implementação de armazenamento será utilizada
    @Autowired
    private StorageProperties storageProperties;

    @Override
    public void storeFile(NewPhoto newPhoto) {
        try {
            Path pathFile = getFilePath(newPhoto.getFileName());
            // copia o fluxo do file para o pathFile 
            FileCopyUtils.copy(newPhoto.getInputStream(), Files.newOutputStream(pathFile));
        } catch (Exception e) {
            throw new StorageException("It was not possible to store file.", e);
        }
    }

    @Override
    public void removeFile(String fileName) {
        try {
            Path pathFile = getFilePath(fileName);
            // remove o arquivo se existir
            Files.deleteIfExists(pathFile);
        } catch (Exception e) {
            throw new StorageException("It was not possible to remove file.", e);
        }
    }

    @Override
    public InputStream recoverFile(String fileName) {
        try {
            Path pathFile = getFilePath(fileName);
            return Files.newInputStream(pathFile);      
        } catch (Exception e) {
            throw new StorageException("It was not possible to recover file.", e);
        }
    }

    /*
     * Método para obter o caminho do arquivo de acordo com o nome do arquivo
     * @param fileName
     * @return Path
     * O resolve é um método da classe Path que concatena o diretório de armazenamento com o nome do arquivo
     */
    private Path getFilePath(String fileName) {
        return storageProperties.getLocal().getDirectory().resolve(Path.of(fileName));
    }
    
}
