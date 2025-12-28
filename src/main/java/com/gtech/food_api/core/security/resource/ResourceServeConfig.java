package com.gtech.food_api.core.security.resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração do Resource Server OAuth2.
 * 
 * Esta classe configura o servidor de recursos (Resource Server) que valida tokens
 * OAuth2 emitidos pelo Authorization Server. O Resource Server é responsável por:
 * - Validar tokens de acesso recebidos nas requisições
 * - Proteger os endpoints da API (exceto os endpoints do Authorization Server)
 * - Verificar escopos e permissões dos tokens
 * 
 * IMPORTANTE: Esta configuração tem ordem menor que o Authorization Server para garantir
 * que os endpoints /oauth2/** sejam tratados primeiro pelo Authorization Server.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ResourceServeConfig {
    
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
    private String introspectionUri;
    
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-client-id}")
    private String clientId;
    
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-client-secret}")
    private String clientSecret;
    
    /**
     * Configura o introspector de tokens opacos.
     * 
     * Este bean é responsável por validar tokens opacos fazendo requisições
     * ao endpoint de introspecção do Authorization Server.
     * 
     * @return OpaqueTokenIntrospector configurado com as credenciais do cliente
     */
    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector() {
        // Usando construtor direto (deprecado mas funcional até versão futura)
        // As propriedades são injetadas via @Value do application.properties
        return new SpringOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
    }
    
    /**
     * Configura o filtro de segurança para o Resource Server.
     * 
     * Esta configuração:
     * - Permite acesso público aos endpoints do Authorization Server (/oauth2/**)
     *   para que o Authorization Server possa processar as requisições de token
     * - Exige autenticação OAuth2 para todos os outros endpoints da API
     * - Configura o Resource Server para usar tokens opacos (opaque tokens)
     * - Desabilita CSRF para APIs REST (não necessário para APIs stateless)
     * - Mantém CORS habilitado para permitir requisições do frontend
     * 
     * A ordem @Order(Ordered.LOWEST_PRECEDENCE - 1) garante que esta configuração
     * seja aplicada DEPOIS do Authorization Server, permitindo que os endpoints
     * /oauth2/** sejam tratados primeiro.
     * 
     * As propriedades de configuração do Resource Server são lidas do application.properties:
     * - spring.security.oauth2.resourceserver.opaquetoken.introspection-uri
     * - spring.security.oauth2.resourceserver.opaquetoken.introspection-client-id
     * - spring.security.oauth2.resourceserver.opaquetoken.introspection-client-secret
     * 
     * @param http objeto HttpSecurity usado para configurar a segurança HTTP
     * @return SecurityFilterChain configurado para o Resource Server
     * @throws Exception caso ocorra algum erro na configuração
     */
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers("/oauth2/**").permitAll() // Permite acesso aos endpoints do Authorization Server
                    .anyRequest().authenticated() // Exige autenticação para todos os outros endpoints
            )
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST stateless
            .oauth2ResourceServer(oauth2 -> 
                oauth2.opaqueToken(opaqueToken -> 
                    opaqueToken.introspector(opaqueTokenIntrospector())
                )
            );
        
        return http.build();
    }
}
