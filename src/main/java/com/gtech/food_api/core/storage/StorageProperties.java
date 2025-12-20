package com.gtech.food_api.core.storage;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Classe de propriedades de configuração para armazenamento de arquivos.
 * 
 * Funcionalidade:
 * - Centraliza todas as configurações relacionadas ao armazenamento de arquivos (fotos de produtos)
 * - Suporta dois tipos de armazenamento: Local (disco) e S3 (Amazon AWS)
 * - Permite alternar entre tipos de armazenamento via arquivo application.properties
 * 
 * Como funciona:
 * - @ConfigurationProperties: Vincula propriedades do application.properties com prefixo "foodapi.storage"
 * - @Component: Registra como bean do Spring para injeção de dependência
 * 
 * Exemplo de uso no application.properties:
 * foodapi.storage.local.directory=/caminho/para/fotos
 * foodapi.storage.s3.keyId=AKIAIOSFODNN7EXAMPLE
 * foodapi.storage.s3.secretKey=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
 * foodapi.storage.s3.bucket=meu-bucket-fotos
 * foodapi.storage.s3.region=us-east-1
 * foodapi.storage.s3.directory=fotos-produtos
 */
@Getter
@Setter
@Component
@ConfigurationProperties("foodapi.storage") // prefixo para as propriedades do storage
public class StorageProperties {
    
    /**
     * Configurações para armazenamento local (disco do servidor).
     * 
     * Quando usar:
     * - Ambiente de desenvolvimento
     * - Aplicações pequenas sem necessidade de escalabilidade
     * - Quando não há necessidade de acesso distribuído aos arquivos
     */
    private Local local = new Local();
    
    /**
     * Configurações para armazenamento na AWS S3 (Amazon Simple Storage Service).
     * 
     * Quando usar:
     * - Ambiente de produção
     * - Aplicações que precisam de escalabilidade
     * - Quando múltiplos servidores precisam acessar os mesmos arquivos
     * - Quando há necessidade de CDN e alta disponibilidade
     */
    private S3 s3 = new S3();

    @Getter
    @Setter
    public class Local {
        private Path directory;
    }

    /**
     * Classe interna para configurações de armazenamento AWS S3.
     * 
     * Propriedades:
     * - keyId: ID da chave de acesso AWS (Access Key ID)
     * - secretKey: Chave secreta de acesso AWS (Secret Access Key)
     * - bucket: Nome do bucket S3 onde os arquivos serão armazenados
     * - region: Região AWS onde o bucket está localizado (ex: us-east-1, sa-east-1)
     * - directory: Diretório/pasta dentro do bucket onde as fotos serão organizadas
     */
    @Getter
    @Setter
    public class S3 {
        private String keyId;
        private String secretKey;
        private String bucket;
        private String region;
        private String directory;
    }
}
