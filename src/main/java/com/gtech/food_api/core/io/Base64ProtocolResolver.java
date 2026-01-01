package com.gtech.food_api.core.io;

import java.util.Base64;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Resolvedor de protocolo customizado para carregar recursos codificados em Base64.
 * 
 * Esta classe permite que o Spring Framework reconheça e carregue recursos usando
 * um protocolo customizado "base64:", onde o conteúdo do arquivo está codificado
 * diretamente na URL como uma string Base64.
 * 
 * COMO FUNCIONA:
 * 
 * 1. ProtocolResolver: Interface do Spring que permite criar protocolos customizados
 *    além dos padrões (file:, classpath:, http:, etc). Quando o Spring encontra uma
 *    URL que começa com "base64:", ele chama este resolvedor.
 * 
 * 2. ApplicationContextInitializer: Interface que permite executar código ANTES do
 *    contexto do Spring ser totalmente inicializado. Aqui, registramos o resolvedor
 *    para que ele esteja disponível desde o início.
 * 
 * 3. Registro Automático: A classe é registrada em META-INF/spring.factories, fazendo
 *    com que o Spring a inicialize automaticamente ao iniciar a aplicação.
 * 
 * CASOS DE USO:
 * 
 * - Carregar configurações embutidas em código (sem arquivos externos)
 * - Carregar recursos pequenos codificados diretamente em propriedades
 * - Simplificar deployment (não precisa de arquivos adicionais)
 * - Carregar certificados/chaves codificadas em Base64 de variáveis de ambiente
 * 
 * EXEMPLO DE USO:
 * 
 *  Em vez de: classpath:keystore/foodapi.jks
 *  Pode usar: base64:UEsDBBQAAAAIAGx...
 * 
 * Resource resource = resourceLoader.getResource("base64:UEsDBBQAAAAIAGx...");
 * 
 * O Spring automaticamente detectará o protocolo "base64:" e chamará este resolvedor
 * para decodificar o conteúdo e retornar um ByteArrayResource.
 */
public class Base64ProtocolResolver implements ProtocolResolver, ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * Resolve uma URL que usa o protocolo "base64:".
     * 
     * Quando o Spring encontra uma URL como "base64:UEsDBBQAAAAIAGx...", este método
     * é chamado para decodificar o conteúdo Base64 e retornar um Resource.
     * 
     * PROCESSAMENTO:
     * 1. Verifica se a URL começa com "base64:"
     * 2. Extrai a parte após "base64:" (substring(7) remove "base64:")
     * 3. Decodifica a string Base64 em um array de bytes
     * 4. Retorna um ByteArrayResource contendo os bytes decodificados
     * 
     * @param location URL do recurso (ex: "base64:UEsDBBQAAAAIAGx...")
     * @param resourceLoader Loader de recursos do Spring (não usado aqui, mas necessário pela interface)
     * @return ByteArrayResource com o conteúdo decodificado, ou null se não for protocolo base64
     */
    @Override
    public Resource resolve(String location, ResourceLoader resourceLoader) {
        if (location.startsWith("base64:")) {
            // Extrai a parte Base64 (remove "base64:" que tem 7 caracteres)
            String base64Content = location.substring(7);
            
            // Decodifica a string Base64 em um array de bytes
            byte[] decoded = Base64.getDecoder().decode(base64Content);
            
            // Retorna um Resource contendo os bytes decodificados
            return new ByteArrayResource(decoded);
        }
        
        // Retorna null se não for protocolo base64 (outros resolvedores tentarão)
        return null;
    }

    /**
     * Registra este resolvedor no contexto do Spring durante a inicialização.
     * 
     * Este método é chamado ANTES do contexto do Spring ser totalmente inicializado,
     * permitindo que registremos o resolvedor de protocolo customizado para que ele
     * esteja disponível desde o início da aplicação.
     * 
     * O registro é feito através do spring.factories (META-INF/spring.factories),
     * que faz com que o Spring chame este método automaticamente.
     * 
     * @param applicationContext Contexto configurável do Spring onde o resolvedor será registrado
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addProtocolResolver(this);
    }
    
}
