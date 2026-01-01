package com.gtech.food_api.core.security.authorization.consents;

import java.security.Principal;
import java.util.List;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

/**
 * Controller responsável por gerenciar a página "Clients Conectados" do OAuth2.
 * 
 * Permite que usuários visualizem e revoguem o acesso de aplicações clientes OAuth2
 * que já foram autorizadas. Funcionalidade similar ao "Aplicativos Conectados" do Google/Facebook.
 */
@Controller
@RequiredArgsConstructor
public class AuthorizedClientsController {
    
    private final OAuth2AuthorizationQueryService oAuth2AuthorizationQueryService;
    private final RegisteredClientRepository clientRepository;
    private final OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService;
    private final OAuth2AuthorizationService auth2AuthorizationService;

    // Lista todas as aplicações clientes OAuth2 para as quais o usuário já concedeu acesso.
    @GetMapping("/oauth2/authorized-clients")
    public String clientList(Principal principal, Model model) {
        // Busca todos os clientes para os quais o usuário já deu consentimento
        List<RegisteredClient> clients = oAuth2AuthorizationQueryService.listClientConsents(principal.getName());
        model.addAttribute("clients", clients);
        return "pages/authorized-clients";
    }

    // Revoga o acesso de uma aplicação cliente OAuth2.
    @PostMapping("/oauth2/authorized-clients/revoke")
    public String revoke(
        Principal principal,
        Model model,
        @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId
    ) {
        // Busca o cliente pelo clientId
        RegisteredClient client = this.clientRepository.findByClientId(clientId);

        if (client == null) {
            throw new AccessDeniedException(String.format("Client not found: %s", clientId));
        }

        // Busca o consentimento (permissões concedidas) do usuário para este cliente
        OAuth2AuthorizationConsent consent = this.oAuth2AuthorizationConsentService.findById(client.getId(), principal.getName());

        // Busca todas as autorizações ativas (tokens, códigos) do usuário para este cliente
        List<OAuth2Authorization> authorizations = this.oAuth2AuthorizationQueryService.listAuthorizations(principal.getName(), client.getId());

        // Remove o consentimento (se existir)
        if (consent != null) {
            this.oAuth2AuthorizationConsentService.remove(consent);
        }

        // Remove todas as autorizações ativas (tokens, códigos de autorização)
        // Isso invalida todos os tokens emitidos para esta aplicação
        for (OAuth2Authorization authorization : authorizations) {
            this.auth2AuthorizationService.remove(authorization);
        }

        // Redireciona de volta para a lista de clientes autorizados
        return "redirect:/oauth2/authorized-clients";
    }
    
}
