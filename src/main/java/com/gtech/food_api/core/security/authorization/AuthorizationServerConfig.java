package com.gtech.food_api.core.security.authorization;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthorizationServerConfig {
    
    /**
     * Configura o filtro de segurança específico para o servidor de autorização OAuth2.
     * 
     * Este método cria uma cadeia de filtros de segurança que será aplicada aos endpoints
     * do servidor de autorização (como /oauth2/token, /oauth2/authorize, etc.).
     * 
     * A anotação @Order(Ordered.HIGHEST_PRECEDENCE) garante que esta configuração seja
     * processada ANTES de qualquer outra configuração de segurança na aplicação. Isso é
     * crucial porque os endpoints do OAuth2 precisam estar disponíveis antes de qualquer
     * outra regra de segurança ser aplicada.
     * 
     * O método applyDefaultSecurity() aplica as configurações padrão do Spring Security
     * para um servidor de autorização OAuth2, incluindo:
     * - Endpoints para emissão de tokens (/oauth2/token)
     * - Endpoints para autorização (/oauth2/authorize)
     * - Endpoints para validação de tokens (/oauth2/introspect)
     * - Configurações de CORS e CSRF apropriadas
     * 
     * @param http objeto HttpSecurity usado para configurar a segurança HTTP
     * @return SecurityFilterChain configurado para o servidor de autorização
     * @throws Exception caso ocorra algum erro na configuração
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.build();
    }

    /**
     * Configura as propriedades do provedor client OAuth2.
     * 
     * O ProviderSettings define informações sobre o servidor de autorização que serão
     * usadas pelos clients OAuth2 para descobrir e interagir com o servidor.
     * 
     * O "issuer" é a URL base do servidor de autorização. Esta URL é usada para:
     * - Identificar unicamente o servidor de autorização
     * - Construir URLs de endpoints (como /oauth2/token)
     * - Validar tokens emitidos por este servidor
     * - Descoberta automática de configurações (se implementado)
     * 
     * Exemplo: Se o issuer for "https://api.exemplo.com", os endpoints serão:
     * - https://api.exemplo.com/oauth2/token
     * - https://api.exemplo.com/oauth2/authorize
     * 
     * @param properties propriedades de segurança da aplicação contendo a URL do provedor
     * @return ProviderSettings configurado com a URL do issuer
     */
    @Bean
    public AuthorizationServerSettings providerSettings(ApiSecurityProperties properties) {
        return AuthorizationServerSettings.builder()
            .issuer(properties.getProviderUrl())
            .build();
    }

    /**
     * Configura o repositório de clientes OAuth2 registrados.
     * 
     * fluxo de segurança:
     * User -> Client -> Authorization Server -> Token -> Resource Server
     * 
     * Este método cria e registra os clients OAuth2 que têm permissão para solicitar
     * tokens de acesso ao servidor de autorização. Um cliente OAuth2 é uma aplicação
     * (como um frontend, mobile app, ou outro backend) que precisa acessar recursos protegidos.
     * 
     * Neste caso, estamos usando um repositório em memória (InMemoryRegisteredClientRepository),
     * o que significa que os clientes são armazenados apenas durante a execução da aplicação.
     * Para produção, seria recomendado usar um repositório persistente (como banco de dados).
     * 
     * Configurações do cliente registrado:
     * 
     * - clientId: Identificador único do cliente ("food-api-backend")
     * - clientSecret: Senha do cliente, criptografada usando PasswordEncoder
     * - clientAuthenticationMethod: Método de autenticação CLIENT_SECRET_BASIC significa
     *   que o cliente enviará suas credenciais via HTTP Basic Authentication no header
     *   Authorization: Basic base64(clientId:clientSecret)
     * 
     * - authorizationGrantType: CLIENT_CREDENTIALS é um fluxo OAuth2 onde o cliente
     *   autentica diretamente usando suas próprias credenciais (sem usuário). Ideal para
     *   comunicação entre serviços (server-to-server), como APIs que precisam se comunicar.
     * 
     * - scope: Define o escopo de permissões que o token terá. "READ" indica que o token
     *   permite apenas operações de leitura. Escopos adicionais "WRITE", "DELETE", etc.
     * 
     * - tokenSettings: Configurações relacionadas aos tokens emitidos:
     *   * accessTokenFormat: REFERENCE significa que o token é "opaco" (opaque), ou seja,
     *     não contém informações legíveis no payload do token. Para validar o token, é necessário consultar
     *     o servidor de autorização. Alternativa seria SELF_CONTAINED (JWT), onde o token
     *     contém todas as informações necessárias.
     *   * accessTokenTimeToLive: Define o tempo de vida do token (30 minutos). 
     * 
     * @param passwordEncoder encoder de senha usado para criptografar o clientSecret
     * @return RegisteredClientRepository contendo os clientes registrados
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient registeredClient = RegisteredClient
            .withId("1") 
            .clientId("food-api-backend") 
            .clientSecret(passwordEncoder.encode("123456")) 
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) 
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) 
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope("READ") 
            .tokenSettings(TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.REFERENCE) 
                .accessTokenTimeToLive(Duration.ofMinutes(30)) 
                .reuseRefreshTokens(false)
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .build())
            .build();
            
        return new InMemoryRegisteredClientRepository(Arrays.asList(registeredClient));

    }
}
