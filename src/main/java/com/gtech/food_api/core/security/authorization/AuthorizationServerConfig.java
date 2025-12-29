package com.gtech.food_api.core.security.authorization;

import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
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
        return http.formLogin(Customizer.withDefaults()).build();
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
     * Fluxo de segurança:
     * User -> Client -> Authorization Server -> Token -> Resource Server
     * 
     * Este método cria e registra três clients OAuth2 que têm permissão para solicitar
     * tokens de acesso ao servidor de autorização. Um cliente OAuth2 é uma aplicação
     * (como um frontend, mobile app, ou outro backend) que precisa acessar recursos protegidos.
     * Podemos criar quantos clients quisermos.
     * 
     * Neste caso, estamos usando um repositório em memória (InMemoryRegisteredClientRepository),
     * o que significa que os clientes são armazenados apenas durante a execução da aplicação.
     * Para produção, seria recomendado usar um repositório persistente (como banco de dados).
     * 
     * Clients registrados:
     * 
     * 1. webClient (food-api-users):
     *    - ID: "1"
     *    - clientId: "food-api-users"
     *    - Fluxo: AUTHORIZATION_CODE + REFRESH_TOKEN (fluxo completo com usuário)
     *    - Scopes: READ e WRITE
     *    - Redirect URIs: http://localhost:8080/authorized e http://localhost:8080/swagger-ui/oauth2-redirect.html
     *    - Requer consentimento do usuário: true
     *    - Tokens: REFERENCE format, 30 minutos de vida, refresh token de 1 dia
     *    - Uso: Aplicações web que precisam autenticar usuários finais
     * 
     * 2. backendClient (client-credencial):
     *    - ID: "2"
     *    - clientId: "client-credencial"
     *    - Fluxo: CLIENT_CREDENTIALS (autenticação direta sem usuário)
     *    - Scopes: READ apenas
     *    - Tokens: REFERENCE format, 30 minutos de vida, sem refresh token
     *    - Uso: Comunicação entre serviços backend (machine-to-machine)
     * 
     * 3. analyticsClient (analytics-client):
     *    - ID: "3"
     *    - clientId: "analytics-client"
     *    - Fluxo: AUTHORIZATION_CODE + REFRESH_TOKEN (fluxo completo com usuário)
     *    - Scopes: READ e WRITE
     *    - Redirect URI: http://foodanalytics.local:8082
     *    - Requer consentimento do usuário: false (autorização automática)
     *    - Tokens: REFERENCE format, 30 minutos de vida, refresh token de 1 dia
     *    - Uso: Aplicação de analytics que precisa acesso automático sem consentimento
     * 
     * Configurações comuns:
     * 
     * - clientSecret: Senha do cliente, criptografada usando PasswordEncoder (BCrypt)
     * - clientAuthenticationMethod: CLIENT_SECRET_BASIC significa que o cliente enviará
     *   suas credenciais via HTTP Basic Authentication no header Authorization: Basic base64(clientId:clientSecret)
     * 
     * - authorizationGrantType:
     *   * AUTHORIZATION_CODE: Fluxo onde o usuário autoriza o cliente e recebe um código,
     *     que é trocado por um token de acesso. Requer interação do usuário.
     *   * CLIENT_CREDENTIALS: Fluxo onde o cliente autentica diretamente usando suas próprias
     *     credenciais (sem usuário). Ideal para comunicação entre serviços backend.
     *   * REFRESH_TOKEN: Permite renovar tokens de acesso sem reautenticação do usuário.
     * 
     * - scope: Define o escopo de permissões que o token terá:
     *   * READ: Permite apenas operações de leitura
     *   * WRITE: Permite operações de escrita
     * 
     * - tokenSettings: Configurações relacionadas aos tokens emitidos:
     *   * accessTokenFormat: REFERENCE significa que o token é "opaco" (opaque), ou seja,
     *     não contém informações legíveis no payload. Para validar, é necessário consultar
     *     o servidor de autorização. Alternativa seria SELF_CONTAINED (JWT).
     *   * accessTokenTimeToLive: Tempo de vida do token de acesso (30 minutos)
     *   * reuseRefreshTokens: Define se o refresh token pode ser reutilizado.
     *     false = revoga o antigo quando um novo é emitido
     *   * refreshTokenTimeToLive: Tempo de vida do refresh token (1 dia)
     * 
     * - redirectUri: URL para onde o usuário será redirecionado após autorização
     *   (necessário apenas para fluxos AUTHORIZATION_CODE)
     * 
     * - clientSettings: Configurações específicas do cliente:
     *   * requireAuthorizationConsent: Define se o usuário precisa aprovar explicitamente
     *     o acesso aos escopos solicitados
     * 
     * @param passwordEncoder encoder de senha usado para criptografar o clientSecret
     * @return RegisteredClientRepository contendo os três clientes registrados
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient webClient = RegisteredClient
            .withId("1") 
            .clientId("food-api-users") 
            .clientSecret(passwordEncoder.encode("123456")) 
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) 
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope("READ") 
            .scope("WRITE")
            .tokenSettings(TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.REFERENCE) 
                .accessTokenTimeToLive(Duration.ofMinutes(30)) 
                .reuseRefreshTokens(false)
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .build())
            .redirectUri("http://localhost:8080/authorized")
            .redirectUri("http://localhost:8080/swagger-ui/oauth2-redirect.html")
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(true)
                .build())
            .build();
            
        RegisteredClient backendClient = RegisteredClient
            .withId("2")
            .clientId("client-credencial")
            .clientSecret(passwordEncoder.encode("123456"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .scope("READ")
            .tokenSettings(TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                .accessTokenTimeToLive(Duration.ofMinutes(30))
                .build())
            .build();

        RegisteredClient analyticsClient = RegisteredClient
            .withId("3")
            .clientId("analytics-client")
            .clientSecret(passwordEncoder.encode("123456"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) 
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .scope("READ") 
            .scope("WRITE")
            .tokenSettings(TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.REFERENCE) 
                .accessTokenTimeToLive(Duration.ofMinutes(30)) 
                .reuseRefreshTokens(false)
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .build())
            .redirectUri("http://foodanalytics.local:8082")
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build())
            .build();
            
        return new InMemoryRegisteredClientRepository(Arrays.asList(webClient, backendClient, analyticsClient));
    }

    /**
     * Configura o serviço de autorização OAuth2 usando JDBC para persistir
     * autorizações (tokens, códigos de autorização, etc.) no banco de dados.
     * 
     * A tabela oauth2_authorization é criada pela migration V2.1 e armazena
     * todas as informações relacionadas às autorizações OAuth2.
     */
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService(JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
    }
}
