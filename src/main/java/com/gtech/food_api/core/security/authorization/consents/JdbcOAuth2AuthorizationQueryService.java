package com.gtech.food_api.core.security.authorization.consents;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * Implementação JDBC do serviço de consulta de autorizações OAuth2.
 * 
 * Esta classe utiliza JDBC direto para fazer consultas personalizadas nas tabelas
 * do banco de dados relacionadas a autorizações OAuth2, permitindo queries mais
 * complexas e eficientes do que as APIs padrão do Spring Security OAuth2.
 * 
 * COMO FUNCIONA:
 * 
 * 1. RowMapper: Converte cada linha do ResultSet em objetos Java (RegisteredClient ou OAuth2Authorization)
 * 2. Queries SQL: Consultas com JOINs entre tabelas para buscar dados relacionados
 * 3. JdbcOperations: Executa as queries e aplica os RowMappers para converter resultados
 * 
 * TABELAS UTILIZADAS:
 * - oauth2_authorization_consent: Armazena os consentimentos de permissões (scopes) concedidos
 * - oauth2_registered_client: Armazena os clientes OAuth2 registrados
 * - oauth2_authorization: Armazena as autorizações (tokens, códigos, etc) emitidas
 */
public class JdbcOAuth2AuthorizationQueryService implements OAuth2AuthorizationQueryService {
    
    // Interface do Spring JDBC para executar queries SQL.
    private final JdbcOperations jdbcOperations;
    
    /**
     * RowMapper para converter linhas do ResultSet em objetos RegisteredClient.
     * O RowMapper é responsável por mapear cada linha retornada pela query SQL
     * para um objeto RegisteredClient, preenchendo todos os campos (clientId,
     * clientSecret, redirectUris, scopes, etc) a partir das colunas do banco.
     */
    private final RowMapper<RegisteredClient> registeredClientRowMapper;

    private final RowMapper<OAuth2Authorization> oAuth2AuthorizationRowMapper;

    /**
     * Query SQL para listar todos os clientes OAuth2 para os quais um usuário
     * específico já concedeu consentimento.
     * 
     * A query faz um JOIN entre:
     * - oauth2_authorization_consent: Tabela de consentimentos (contém principal_name)
     * - oauth2_registered_client: Tabela de clientes registrados (contém dados do cliente)
     * 
     * Filtra por principal_name (email/username do usuário) para retornar apenas
     * os clientes que o usuário já autorizou.
     * 
     * Exemplo de resultado: Lista de todas as aplicações (frontend, mobile, etc)
     * para as quais o usuário "joao@email.com" já deu permissão de acesso.
     */
    private final String LIST_AUTHORIZED_CLIENTS = "select rc.* from oauth2_authorization_consent c " +
            "inner join oauth2_registered_client rc on rc.id = c.registered_client_id " +
            "where c.principal_name = ? ";

    /**
     * Query SQL para listar todas as autorizações (tokens, códigos) de um usuário
     * específico para um cliente específico.
     * 
     * A query faz um JOIN entre:
     * - oauth2_authorization: Tabela de autorizações (tokens, códigos, estados)
     * - oauth2_registered_client: Tabela de clientes (para validação)
     * 
     * Filtra por:
     * - principal_name: Usuário que concedeu a autorização
     * - registered_client_id: Cliente OAuth2 específico
     * 
     * Exemplo de uso: Listar todos os tokens ativos que o usuário "joao@email.com"
     * possui para a aplicação "food-api-users".
     */
    private final String LIST_AUTHORIZATIONS_QUERY = "select a.* from oauth2_authorization a " +
            "inner join oauth2_registered_client c on c.id = a.registered_client_id " +
            "where a.principal_name = ? " +
            "and a.registered_client_id  = ? ";

    /**
     * Construtor que inicializa os RowMappers necessários.
     * 
     * Os RowMappers são criados usando as implementações padrão do Spring Security OAuth2,
     * que já conhecem a estrutura das tabelas e sabem como mapear as colunas para os objetos.
     * 
     * @param jdbcOperations Interface para executar queries SQL
     * @param repository Repositório de clientes (necessário para o OAuth2AuthorizationRowMapper
     *                   poder buscar dados adicionais do cliente quando necessário)
     */
    public JdbcOAuth2AuthorizationQueryService(JdbcOperations jdbcOperations, RegisteredClientRepository repository) {
        this.jdbcOperations = jdbcOperations;
        this.registeredClientRowMapper = new JdbcRegisteredClientRepository.RegisteredClientRowMapper();
        this.oAuth2AuthorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(repository);
    }

    /**
     * @param principalName Email ou username do usuário (ex: "joao@email.com")
     * @return Lista de clientes OAuth2 registrados para os quais o usuário já autorizou acesso
     */
    @Override
    public List<RegisteredClient> listClientConsents(String principalName) {
        return jdbcOperations.query(LIST_AUTHORIZED_CLIENTS, registeredClientRowMapper, principalName);
    }

    /**
     * Lista todas as autorizações (tokens, códigos) de um usuário para um cliente específico.
     * 
     * Este método é útil para:
     * - Ver histórico de tokens emitidos para uma aplicação específica
     * - Implementar funcionalidade de "Revogar todos os tokens" de uma app
     * - Auditoria e monitoramento de autorizações
     * 
     * @param principalName Email ou username do usuário (ex: "joao@email.com")
     * @param clientId ID do cliente OAuth2 (ex: "food-api-users")
     * @return Lista de autorizações OAuth2 (pode incluir tokens ativos, códigos de autorização, etc)
     */
    @Override
    public List<OAuth2Authorization> listAuthorizations(String principalName, String clientId) {
        return jdbcOperations.query(LIST_AUTHORIZATIONS_QUERY, oAuth2AuthorizationRowMapper, principalName, clientId);
    }
}
