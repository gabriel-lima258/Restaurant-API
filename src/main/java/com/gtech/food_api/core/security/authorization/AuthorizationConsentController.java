package com.gtech.food_api.core.security.authorization;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

/**
 * Controller responsável por gerenciar a página de consentimento de autorização OAuth2.
 * 
 * CONTEXTO:
 * No fluxo OAuth2, quando uma aplicação cliente (ex: frontend, mobile app) solicita acesso
 * aos recursos do usuário, o servidor de autorização precisa obter o consentimento explícito
 * do usuário antes de conceder as permissões (scopes) solicitadas.
 * 
 * FLUXO DE FUNCIONAMENTO:
 * 
 * 1. Aplicação cliente redireciona o usuário para o Authorization Server com os parâmetros:
 *    - client_id: identificador da aplicação cliente
 *    - scope: lista de permissões solicitadas (ex: "read write")
 *    - state: token CSRF para segurança
 * 
 * 2. Se o usuário ainda não autorizou essas permissões, o Spring Security OAuth2 redireciona
 *    para este endpoint (/oauth2/consent) para exibir a página de consentimento.
 * 
 * 3. Este controller prepara os dados necessários e retorna a view HTML (pages/approval.html)
 *    onde o usuário pode visualizar e aprovar/negar as permissões solicitadas.
 * 
 * 4. Após o usuário aprovar, o formulário é submetido de volta para /oauth2/authorize,
 *    que processa a autorização e retorna o código de autorização ou token.
 * 
 * DIFERENÇA ENTRE @Controller E @RestController:
 * - @Controller: retorna uma view HTML (usado para páginas web)
 * - @RestController: retorna JSON/XML (usado para APIs REST)
 * 
 * Neste caso, usamos @Controller porque precisamos retornar uma página HTML de consentimento.
 */
@Controller
@RequiredArgsConstructor
public class AuthorizationConsentController {
    
    /**
     * Repositório de clientes OAuth2 registrados.
     * 
     * Armazena informações sobre as aplicações clientes que podem solicitar autorização,
     * como client_id, client_secret, redirect_uris, scopes permitidos, etc.
     */
    private final RegisteredClientRepository clientRepository;
    
    /**
     * Serviço que gerencia os consentimentos de autorização já concedidos pelos usuários.
     * 
     * Armazena quais permissões (scopes) cada usuário já autorizou para cada cliente,
     * permitindo que o sistema identifique quais permissões são novas e precisam de aprovação.
     */
    private final OAuth2AuthorizationConsentService authorizationConsentService;

    /**
     * Endpoint que exibe a página de consentimento de autorização OAuth2.
     * @param principal Objeto que representa o usuário autenticado (contém username, etc)
     * @param model Objeto do Spring MVC usado para passar dados para a view HTML
     * @param clientId ID da aplicação cliente que está solicitando autorização
     * @param scope String contendo as permissões solicitadas, separadas por espaço (ex: "read write delete")
     * @param state Token CSRF enviado pela aplicação cliente para prevenir ataques
     * @return Nome da view HTML que será renderizada (pages/approval)
     */
    @GetMapping("/oauth2/consent")
    public String consent(
        Principal principal,
        Model model,
        @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
        @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
        @RequestParam(OAuth2ParameterNames.STATE) String state
    ) {
        // Busca o cliente OAuth2 registrado no sistema usando o client_id recebido
        RegisteredClient client = this.clientRepository.findByClientId(clientId);

        // Validação de segurança: se o cliente não foi encontrado, lança exceção
        if (client == null) {
            throw new AccessDeniedException("client not found: " + clientId);
        }

        // Busca se já existe um consentimento prévio deste usuário para este cliente
        OAuth2AuthorizationConsent consent = this.authorizationConsentService.findById(
            client.getId(), 
            principal.getName()
        );

        // Converte a string de scopes (ex: "read write delete") em um array de strings
        // O delimitador é o espaço em branco
        String[] scopeArray = StringUtils.delimitedListToStringArray(scope, " ");
        
        // Converte o array em um Set para facilitar operações de conjunto (união, interseção, etc)
        Set<String> scopesParaAprovar = new HashSet<>(Set.of(scopeArray));

        // Variável que armazenará as permissões que o usuário já aprovou anteriormente
        Set<String> scopesAprovadosAnteriormente;
        
        // Se já existe um consentimento prévio
        if (consent != null) {
            // Obtém todas as permissões que o usuário já autorizou para este cliente
            scopesAprovadosAnteriormente = consent.getScopes();
            
            // Remove das permissões para aprovar aquelas que já foram aprovadas anteriormente
            // Isso evita que o usuário precise aprovar novamente permissões que já concedeu
            scopesParaAprovar.removeAll(scopesAprovadosAnteriormente);
        } else {
            // Se não existe consentimento prévio, significa que é a primeira vez que este
            scopesAprovadosAnteriormente = Collections.emptySet();
        }

        // Adiciona os dados ao modelo do Spring MVC para que a view HTML possa acessá-los
        // usando Thymeleaf (ex: ${clientId}, ${state}, etc)
        
        // ID do cliente para exibir na página
        model.addAttribute("clientId", clientId);
        
        // Token CSRF que será enviado de volta no formulário para manter a segurança
        model.addAttribute("state", state);
        
        // Nome do usuário autenticado para personalizar a mensagem
        model.addAttribute("principalName", principal.getName());
        
        // Permissões novas que precisam ser aprovadas pelo usuário agora
        // Estas aparecerão como checkboxes habilitados na página
        model.addAttribute("scopesParaAprovar", scopesParaAprovar);
        
        // Permissões que o usuário já aprovou anteriormente
        // Estas aparecerão como checkboxes desabilitados e marcados na página
        // (apenas para informação, não precisam ser aprovadas novamente)
        model.addAttribute("scopesAprovadosAnteriormente", scopesAprovadosAnteriormente);

        // Retorna o nome da view HTML que será renderizada
        return "pages/approval";
    }
}
