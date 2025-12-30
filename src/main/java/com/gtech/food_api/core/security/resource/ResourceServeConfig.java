package com.gtech.food_api.core.security.resource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Esta classe configura o servidor de recursos (Resource Server) que valida tokens
 * OAuth2 emitidos pelo Authorization Server.
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
                .requestMatchers("/authorized").permitAll() // Permite acesso ao callback OAuth2
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll() // Swagger UI
                .requestMatchers("/v3/api-docs/**", "/swagger-resources/**").permitAll() // OpenAPI docs
                .requestMatchers("/oauth2/**").authenticated()
                .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST stateless
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        
        return http.formLogin(Customizer.withDefaults()).build();
    }

    /**
     * Configura o conversor de JWT para Authentication do Spring Security.
     * 
     * Este método é ESSENCIAL para extrair as permissões (authorities) do token JWT
     * e disponibilizá-las para o Spring Security usar na autorização dos endpoints.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Cria o conversor principal que transforma JWT em Authentication
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    
        // Configura um conversor customizado de authorities (permissões)
        // Esta função lambda será chamada para CADA requisição autenticada
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // PASSO 1: Extrair SCOPES (authorities padrão do OAuth2)
            // 
            // JwtGrantedAuthoritiesConverter: Conversor padrão do Spring Security
            // Extrai a claim "scope" do JWT e converte em GrantedAuthority
            // 
            // Exemplo de claim "scope" no JWT:
            // "scope": "READ WRITE"
            // 
            // Resultado da conversão:
            // [SCOPE_READ, SCOPE_WRITE]
            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
            
            // Converte os scopes do token em GrantedAuthority
            // grantedAuthorities agora contém: SCOPE_READ, SCOPE_WRITE
            Collection<GrantedAuthority> grantedAuthorities  = authoritiesConverter.convert(jwt);
    
            // PASSO 2: Extrair AUTHORITIES CUSTOMIZADAS (adicionadas pelo Authorization Server)
            // 
            // A claim "authorities" foi adicionada pelo método jwtCustomizer()
            // no AuthorizationServerConfig durante a geração do token
            // 
            // Exemplo de claim "authorities" no JWT:
            // "authorities": ["ROLE_ADMIN", "EDIT_RESTAURANTS", "DELETE_USERS"]
            // 
            // getClaimAsStringList(): Extrai a claim como List<String>
            // Retorna null se a claim não existir (caso CLIENT_CREDENTIALS)
            List<String> authorities = jwt.getClaimAsStringList("authorities");
    
            // PASSO 3: Verificar se existem authorities customizadas
            // 
            // authorities == null em dois casos:
            // 
            // CASO 1: Token gerado com CLIENT_CREDENTIALS
            // - Não há usuário logado, apenas um cliente OAuth2
            // - O jwtCustomizer não adiciona a claim "authorities"
            // - Token contém apenas scopes (READ, WRITE)
            // - Usado para comunicação máquina-a-máquina
            // 
            // CASO 2: Usuário sem permissões
            // - Improvável, mas possível se o usuário não tiver grupos/roles
            if (authorities == null) {
                // Retorna apenas as authorities dos scopes (SCOPE_READ, SCOPE_WRITE)
                // Exemplo final: [SCOPE_READ, SCOPE_WRITE]
                return grantedAuthorities;
            }
    
            // PASSO 4: MERGE - Combinar scopes + authorities customizadas
            // 
            // Processamento:
            // 1. authorities.stream() - Cria stream da lista de strings
            // 2. .map(SimpleGrantedAuthority::new) - Converte cada String em GrantedAuthority
            // 3. .collect(Collectors.toList()) - Coleta em uma lista
            // 4. grantedAuthorities.addAll() - Adiciona à lista existente (que tem os scopes)
            // 
            // Exemplo de resultado final combinado:
            // [
            //   SCOPE_READ,           ← do scope
            //   SCOPE_WRITE,          ← do scope
            //   ROLE_ADMIN,           ← da claim authorities
            //   EDIT_RESTAURANTS,     ← da claim authorities
            //   DELETE_USERS          ← da claim authorities
            // ]
            grantedAuthorities.addAll(authorities
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
    
            // Retorna a lista UNIFICADA de authorities (scopes + authorities customizadas)
            // Esta lista será usada pelo Spring Security para:
            // - @PreAuthorize("hasAuthority('ROLE_ADMIN')")
            // - @PreAuthorize("hasAuthority('SCOPE_READ')")
            // - SecurityContext.getAuthentication().getAuthorities()
            return grantedAuthorities;
        });
    
        return converter;
    }
}
