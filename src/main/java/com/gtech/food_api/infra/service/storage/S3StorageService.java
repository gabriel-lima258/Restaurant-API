package com.gtech.food_api.infra.service.storage;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.gtech.food_api.core.storage.StorageProperties;
import com.gtech.food_api.domain.service.storage.PhotoStorageService;
import com.gtech.food_api.infra.service.storage.exceptions.StorageException;

public class S3StorageService implements PhotoStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private StorageProperties storageProperties;
    
    @Override
    public void storeFile(NewPhoto newPhoto) {
    try {
        // concatenando o diretório do bucket s3 e o nome do arquivo, ex: catalog/foto.jpg
        String getFilePath = getFilePath(newPhoto.getFileName());

        var objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(newPhoto.getContentType());
        objectMetadata.setContentLength(newPhoto.getContentLength());

        /*
        * O bucket é o nome do bucket S3 onde o arquivo será armazenado.
        * O key é o caminho completo do arquivo no bucket S3.
        * O inputStream é o fluxo de entrada do arquivo.
        * O objectMetadata é o objeto que contém os metadados do arquivo.
        * O withCannedAcl(CannedAccessControlList.PublicRead) define o acesso público ao arquivo.
        */
        var putObjectRequest = new PutObjectRequest(
            storageProperties.getS3().getBucket(), 
            getFilePath,
            newPhoto.getInputStream(),
            objectMetadata)
            .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3.putObject(putObjectRequest);
    } catch (Exception e) {
        throw new StorageException("Error storing file", e);
    }
    }

    @Override
    public void removeFile(String fileName) {
        try {
            String getFilePath = getFilePath(fileName);
            amazonS3.deleteObject(storageProperties.getS3().getBucket(), getFilePath);
        } catch (Exception e) {
            throw new StorageException("Error removing file", e);
        }
    }

    @Override
    public RecoverPhoto recoverFile(String fileName) {
        try {
            String getFilePath = getFilePath(fileName);
            URL url = amazonS3.getUrl(storageProperties.getS3().getBucket(), getFilePath);
            return RecoverPhoto.builder()
                .url(url.toString())
                .build();
        } catch (Exception e) {
            throw new StorageException("Error recovering file", e);
        }
    }

    // pega o diretório do bucket s3 e adiciona o nome do arquivo
    private String getFilePath(String fileName) {
        return String.format("%s/%s", storageProperties.getS3().getDirectory(), fileName);
    }
}
