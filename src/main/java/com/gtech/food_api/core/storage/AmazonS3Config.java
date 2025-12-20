package com.gtech.food_api.core.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Classe de configuração do cliente Amazon S3.
 * 
 * Como funciona:
 * - @Configuration: Indica ao Spring que esta classe contém configurações de beans
 * - @Bean: Registra o método amazonS3() como um bean gerenciado pelo Spring
 * - O bean criado pode ser injetado em qualquer classe usando @Autowired
 */
@Configuration
public class AmazonS3Config {
    
    @Autowired
    private StorageProperties storageProperties;

    /**
     * Cria e configura o cliente Amazon S3 para comunicação com AWS.
     * 
     * O que faz:
     * - BasicAWSCredentials: Encapsula as credenciais de acesso AWS
     * - AWSStaticCredentialsProvider: Fornece as credenciais ao cliente S3
     * - AmazonS3ClientBuilder: Builder pattern para criar o cliente de forma fluente
     * - withRegion: Define a região AWS onde o bucket está localizado
     */
    @Bean
    private AmazonS3 amazonS3() {
        var credentials = new BasicAWSCredentials(storageProperties.getS3().getKeyId(),
            storageProperties.getS3().getSecretKey());

        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(storageProperties.getS3().getRegion())
            .build();
    }
}
