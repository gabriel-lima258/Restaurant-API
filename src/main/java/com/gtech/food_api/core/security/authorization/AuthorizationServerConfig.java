package com.gtech.food_api.core.security.authorization;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings; 
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.gtech.food_api.domain.repository.UserRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {

    /**
     * Configura o filtro de segurança específico para o servidor de autorização OAuth2.
     * 
     * Este método cria uma cadeia de filtros de segurança que será aplicada aos endpoints
     * do servidor de autorização (como /oauth2/token, /oauth2/authorize, etc.).
     * 
     * A anotação @Order(Ordered.HIGHEST_PRECEDENCE) garante que esta configuração seja
     * processada ANTES de qualquer outra configuração de segurança na aplicação.
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
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				new OAuth2AuthorizationServerConfigurer();

        // Configura o endpoint de consentimento para a página de consentimento criada no templates/pages/approval.html
        authorizationServerConfigurer.authorizationEndpoint(customizer -> 
            customizer.consentPage("/oauth2/consent"));

		RequestMatcher endpointsMatcher = authorizationServerConfigurer
				.getEndpointsMatcher();

		http.securityMatcher(endpointsMatcher)
			.authorizeHttpRequests(authorize ->
				authorize.anyRequest().authenticated()
			)
			.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
            .formLogin(Customizer.withDefaults())
            .exceptionHandling(
                exceptions -> exceptions.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
            )
            .apply(authorizationServerConfigurer);

        return http.formLogin(customizer -> customizer.loginPage("/login")).build();
    }


    /**
     * Configura as propriedades do provedor client OAuth2.
     * 
     * O ProviderSettings define informações sobre o servidor de autorização que serão
     * usadas pelos clients OAuth2 para descobrir e interagir com o resource server.
     * 
     * O "issuer" é a URL base do servidor de autorização. Esta URL é usada para:
     * - Identificar unicamente o servidor de AUTHORIZATION SERVER
     * 
     * Exemplo: Se o issuer for "https://api.exemplo.com", os endpoints serão:
     * - https://api.exemplo.com/oauth2/token
     * - https://api.exemplo.com/oauth2/authorize
     */
    @Bean
    public AuthorizationServerSettings providerSettings(ApiSecurityProperties properties) {
        return AuthorizationServerSettings.builder()
            .issuer(properties.getProviderUrl())
            .build();
    }

    /**
     * Configura o repositório de clientes OAuth2 registrados manualmente sem DB. Ideal para api sem varios clients
     * 
     * Fluxo de segurança:
     * User -> Client -> Authorization Server -> Token -> Resource Server
     * Um cliente OAuth2 é uma aplicação (como um frontend, mobile app, ou outro backend) que precisa acessar recursos     protegidos.
     * Podemos criar quantos clients quisermos.
     * 
     * Neste caso, estamos usando um repositório em memória (InMemoryRegisteredClientRepository),
     * o que significa que os clientes são armazenados apenas durante a execução da aplicação.
     * Para produção, seria recomendado usar um repositório persistente (como banco de dados).
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
     *     o servidor de autorização. Precisa de mais infraestrutura para validar, bancos de dados
     *     e vantagens de revogar tokens.
     *   * accessTokenFormat: SELF_CONTAINED significa que o refresh token é "transparente" (JWT), ou seja,
     *     contém informações legíveis no payload do token. Não precisa de mais infraestrutura para validar, não é possivel revogar
     *     tokens e preciso esperar expirar.
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
     */
    // @Bean
    // public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
    //     RegisteredClient webClient = RegisteredClient
    //         .withId("1") 
    //         .clientId("food-api-users") 
    //         .clientSecret(passwordEncoder.encode("123456")) 
    //         .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    //         .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) 
    //         .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
    //         .scope("READ") // scopo são limitações ao client idenpendente de usuário
    //         .scope("WRITE")
    //         .tokenSettings(TokenSettings.builder()
    //             .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED) 
    //             .accessTokenTimeToLive(Duration.ofMinutes(30)) 
    //             .reuseRefreshTokens(false)
    //             .refreshTokenTimeToLive(Duration.ofDays(1))
    //             .build())
    //         .redirectUri("http://127.0.0.1:8080/authorized")
    //         .redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")
    //         .clientSettings(ClientSettings.builder()
    //             .requireAuthorizationConsent(true)
    //             .build())
    //         .build();
            
    //     RegisteredClient backendClient = RegisteredClient
    //         .withId("2")
    //         .clientId("client-credencial")
    //         .clientSecret(passwordEncoder.encode("123456"))
    //         .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    //         .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
    //         .scope("READ")
    //         .tokenSettings(TokenSettings.builder()
    //             .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
    //             .accessTokenTimeToLive(Duration.ofMinutes(30))
    //             .build())
    //         .build();
    //     return new InMemoryRegisteredClientRepository(Arrays.asList(webClient, backendClient));
    // }

    // Cria clients em DB, ideal para api com varios clients
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcOperations jdbcOperations) {
        return new JdbcRegisteredClientRepository(jdbcOperations);  
    }

    @Bean
    public OAuth2AuthorizationConsentService consentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
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

    /**
     * Configura a fonte de chaves JWK (JSON Web Key) para assinatura de tokens JWT.
     * 
     * Este método é responsável por carregar o par de chaves RSA (chave privada + certificado público)
     * do arquivo KeyStore (.jks) e disponibilizá-lo para o Authorization Server usar na assinatura.
     * 
     * Gerado em terminal keytool - Chaves publicas e privadas:
     * keytool -genkeypair -alias algafood -keyalg RSA -keypass senhaprivada -keystore algafood.jks -storepass senhastore -validity 3650
     * 
     * Lista as chaves do KeyStore:
     * keytool -list -keystore algafood.jks
     * 
     * Exporta o arquivo binario para um arquivo PEM textual:
     * keytool -export -rfc -alias algafood -keystore algafood.jks -file algafood-cert.pem
     * 
     * Pega a chave publica do arquivo PEM textual:
     * openssl x509 -pubkey -noout -in algafood-cert.pem > algafood-pkey.pem
     * 
     * Fluxo de funcionamento:
     * 1. Obtém as propriedades de configuração do KeyStore (senha, alias, localização)
     * 2. Carrega o arquivo KeyStore a partir do recurso configurado
     * 3. Extrai o par de chaves RSA usando o alias especificado
     * 4. Cria uma fonte JWK imutável que será usada pelo Authorization Server
     * 
     * Importância:
     * - A chave privada é usada para ASSINAR tokens JWT no Authorization Server
     * - A chave pública correspondente é exposta publicamente /oauth2/jwks
     * - O Resource Server usa a chave pública para VALIDAR a assinatura dos tokens recebidos
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception {
        // Senha para abrir o arquivo KeyStore (.jks)
        char[] keyStorePass = properties.getPassword().toCharArray();
        
        // Nome do par de chave criado no KeyStore
        // Um KeyStore pode conter múltiplos pares de chaves, cada um com seu próprio alias(nome)
        String keypairAlias = properties.getKeypairAlias();

        // Obtém a localização do arquivo KeyStore (.jks)
        Resource jksLocation = properties.getJksLocation();
        
        // Abre um stream de entrada para ler o arquivo KeyStore
        InputStream inputStream = jksLocation.getInputStream();
        
        // Cria uma instância de KeyStore do tipo JKS (Java KeyStore)
        // JKS é o formato padrão de KeyStore da plataforma Java
        KeyStore keyStore = KeyStore.getInstance("JKS");
        
        // Carrega o arquivo e senha do KeyStore (.jks)
        keyStore.load(inputStream, keyStorePass);

        // Extrai o par de chaves RSA do KeyStore usando o alias e a senha
        // RSAKey contém tanto a chave privada (para assinar) quanto o certificado público (para validar)
        // A senha é necessária para descriptografar a chave privada dentro do KeyStore
        RSAKey rsaKey = RSAKey.load(keyStore, keypairAlias, keyStorePass);

        // Cria uma fonte JWK imutável contendo o conjunto de chaves
        // ImmutableJWKSet garante que as chaves não possam ser modificadas após a criação
        // JWKSet é o formato padrão JSON para representar conjuntos de chaves públicas
        // Esta fonte será usada pelo Authorization Server para:
        // - Assinar tokens JWT usando a chave privada
        // - Expor a chave pública no endpoint /oauth2/jwks para validação pelos Resource Servers
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }

    /**
     * Customiza o payload (claims) do token JWT gerado pelo Authorization Server.
     * 
     * Por padrão, o Spring Security gera tokens JWT com claims básicos:
     * - sub: subject (identificador do usuário)
     * - iat: issued at (data de emissão)
     * - exp: expiration (data de expiração)
     * - scope: escopos autorizados (READ, WRITE)
     * 
     * Este customizador ADICIONA claims personalizadas ao token:
     * - user_id: ID do usuário no banco de dados
     * - authorities: Lista de permissões/roles do usuário
     * 
     * Por que adicionar claims customizadas?
     * - O Resource Server (API) pode extrair essas informações diretamente do token
     * - Evita consultas ao banco de dados a cada requisição
     * - Melhora performance e escalabilidade
     * - Permite autorização baseada em roles sem estado (stateless)
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserRepository userRepository) {
        return (context) -> {
            // Obtém o objeto Authentication que representa o usuário/cliente autenticado
            Authentication authentication = context.getPrincipal();
            
            // Verifica o TIPO de autenticação para decidir quais claims adicionar:
            // 
            // 1. User (org.springframework.security.core.userdetails.User):
            //    - Grant type: AUTHORIZATION_CODE (usuário fez login)
            //    - Principal: Objeto User retornado pelo JpaUserDetailsService
            //    - Neste caso, adicionamos user_id e authorities ao token
            // 
            // 2. ClientPrincipal (outro tipo):
            //    - Grant type: CLIENT_CREDENTIALS (autenticação máquina-a-máquina)
            //    - Principal: Objeto representando o cliente OAuth2
            //    - Neste caso, NÃO adicionamos claims customizadas (não há usuário)
            if (authentication.getPrincipal() instanceof User) {
                User user = (User) authentication.getPrincipal();

                com.gtech.food_api.domain.model.User domainUser = userRepository
                    .findByEmail(user.getUsername())
                    .orElseThrow();

                // Extrai as permissões/authorities do usuário autenticado
                Set<String> authorities = new HashSet<>();
                for (GrantedAuthority authority : user.getAuthorities()) {
                    authorities.add(authority.getAuthority());
                }

                // Adiciona CLAIMS CUSTOMIZADAS ao payload do token JWT
                // 
                // Claims são pares chave-valor que ficam dentro do token
                // Estrutura final do token JWT decodificado:
                // {
                //   "sub": "joao@email.com",           // claim padrão
                //   "iat": 1672531200,                 // claim padrão
                //   "exp": 1672534800,                 // claim padrão
                //   "scope": "READ WRITE",             // claim padrão
                //   "user_id": 123,                    // ← CLAIM CUSTOMIZADA
                //   "authorities": ["ROLE_ADMIN"]      // ← CLAIM CUSTOMIZADA
                // }
                context.getClaims().claim("user_id", domainUser.getId().toString());
                context.getClaims().claim("authorities", authorities);
            }
        };
    }
}
