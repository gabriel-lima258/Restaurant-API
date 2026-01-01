package com.gtech.food_api.core.security.authorization.consents;

import java.util.List;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

/**
 * Interface de serviço para consultar informações sobre autorizações OAuth2.
 * 
 * Esta interface fornece métodos para buscar dados relacionados às autorizações
 * OAuth2 concedidas pelos usuários, permitindo consultas personalizadas que não
 * estão disponíveis diretamente no OAuth2AuthorizationService padrão do Spring Security.
 * 
 * CASOS DE USO:
 * - Listar todas as aplicações clientes para as quais um usuário já deu consentimento
 * - Consultar histórico de autorizações de um usuário específico
 * - Implementar funcionalidades de gerenciamento de permissões (ex: página "Aplicações Conectadas")
 * - Revogar consentimento e autorizações de um cliente específico
 * 
 * IMPLEMENTAÇÃO:
 * A implementação padrão (JdbcOAuth2AuthorizationQueryService) utiliza JDBC para
 * fazer consultas diretas nas tabelas do banco de dados, permitindo queries mais
 * complexas e eficientes do que as APIs padrão do Spring Security OAuth2.
 */
public interface OAuth2AuthorizationQueryService {
    
    List<RegisteredClient> listClientConsents(String principalName);
    List<OAuth2Authorization> listAuthorizations(String principalName, String clientId);
}
