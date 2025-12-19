package com.gtech.food_api.infra.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.gtech.food_api.domain.service.PhotoStorageService;
import com.gtech.food_api.infra.service.storage.exceptions.StorageException;

@Service
public class PhotoLocalStorageService implements PhotoStorageService {

    @Value("${photo.storage.local.directory}")
    private Path directoryPath;

    @Override
    public void store(NewPhoto newPhoto) {
        try {
            Path pathFile = getFilePath(newPhoto.getFileName());
            // copia o fluxo do file para o pathFile 
            FileCopyUtils.copy(newPhoto.getInputStream(), Files.newOutputStream(pathFile));
        } catch (Exception e) {
            throw new StorageException("It was not possible to store file.", e);
        }
    }

    @Override
    public void remove(String fileName) {
        try {
            Path pathFile = getFilePath(fileName);
            // remove o arquivo se existir
            Files.deleteIfExists(pathFile);
        } catch (Exception e) {
            throw new StorageException("It was not possible to remove file.", e);
        }
    }

    /*
     * Método para obter o caminho do arquivo de acordo com o nome do arquivo
     * @param fileName
     * @return Path
     * O resolve é um método da classe Path que concatena o diretório de armazenamento com o nome do arquivo
     */
    private Path getFilePath(String fileName) {
        return directoryPath.resolve(Path.of(fileName));    
    }
    
}
