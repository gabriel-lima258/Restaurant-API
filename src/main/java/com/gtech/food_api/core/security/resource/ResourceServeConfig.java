package com.gtech.food_api.core.security.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

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

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    /**
     * Configura o filtro de segurança para o Resource Server.
     * 
     * Esta configuração:
     * - Permite acesso público aos endpoints do Authorization Server (/oauth2/**)
     *   para que o Authorization Server possa processar as requisições de token
     * - Permite acesso público ao endpoint /authorized (callback OAuth2)
     * - Permite acesso público ao Swagger UI (/swagger-ui/**)
     * - Exige autenticação OAuth2 para todos os outros endpoints da API
     * - Configura o Resource Server para usar tokens JWT (JSON Web Tokens)
     * - Desabilita CSRF para APIs REST (não necessário para APIs stateless)
     * - Mantém CORS habilitado para permitir requisições do frontend
     * 
     * A ordem @Order(Ordered.LOWEST_PRECEDENCE - 1) garante que esta configuração
     * seja aplicada DEPOIS do Authorization Server, permitindo que os endpoints
     * /oauth2/** sejam tratados primeiro.
     * 
     * As propriedades de configuração do Resource Server são lidas do application.properties:
     * - spring.security.oauth2.resourceserver.jwt.jwk-set-uri: URI do endpoint JWKS
     *   do Authorization Server para obter as chaves públicas para validação de tokens JWT
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
                    .requestMatchers("/oauth2/**").authenticated()
                    .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST stateless
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        
        return http.formLogin(Customizer.withDefaults()).build();
    }
}
