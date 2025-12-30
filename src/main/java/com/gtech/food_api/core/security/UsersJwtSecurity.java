package com.gtech.food_api.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Utilitário para acessar informações do usuário autenticado via JWT.
 * 
 * UTILIDADE PRINCIPAL:
 * Esta classe facilita o acesso aos dados do usuário logado que está
 * fazendo uma requisição à API, extraindo informações diretamente do
 * token JWT sem precisar consultar o banco de dados.
 * 
 * COMO FUNCIONA:
 * 1. Cliente envia requisição com header: Authorization: Bearer <token_jwt>
 * 2. Resource Server valida o token e extrai as claims
 * 3. Spring Security armazena o token no SecurityContext (thread-local)
 * 4. Esta classe acessa o SecurityContext e extrai informações do JWT
 * 5. Retorna os dados sem precisar consultar o banco
 */
@Component
public class UsersJwtSecurity {
    
    /**
     * Obtém o objeto Authentication do usuário autenticado na requisição atual.
     * 
     * O que é Authentication?
     * - Interface do Spring Security que representa o usuário autenticado
     * 
     * O que é SecurityContextHolder?
     * - Classe estática que armazena o SecurityContext da thread atual
     * - SecurityContext: Container que guarda o Authentication
     * - Thread-local: Cada thread (requisição HTTP) tem seu próprio SecurityContext
     * 
     * Fluxo:
     * 1. Requisição chega com token JWT no header Authorization
     * 2. Resource Server valida o token (jwtAuthenticationConverter)
     * 3. Cria um objeto Authentication com os dados do JWT
     * 4. Armazena no SecurityContext da thread atual
     * 5. Este método acessa o SecurityContext e retorna o Authentication
     */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    // Extrai o ID do usuário autenticado da claim customizada "user_id" do JWT.
    // PASSO 1: Obtém o Authentication da thread atual
    // PASSO 2: Extrai o Principal (o objeto principal do usuário autenticado)
    // O que é Principal?
    // - No caso de autenticação JWT, o Principal é um objeto Jwt
    // - Jwt contém todas as claims do token (sub, exp, user_id, authorities, etc.)
    // - Cast (Jwt) é seguro porque sabemos que usamos JWT no Resource Server
    // PASSO 3: Extrai a claim "user_id" do payload do JWT
    // jwt.getClaim("user_id"): Retorna Object porque claims podem ser de qualquer tipo
    // - String, Long, List, Map, etc.
    // PASSO 4: Verificar se a claim existe
    // Quando userId é null?
    // - Token gerado com grant_type=CLIENT_CREDENTIALS (sem usuário, apenas cliente)
    // Quando userId existe?
    // - Token gerado com AUTHORIZATION_CODE (usuário fez login)
    public Long getUserId() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        Object userId = jwt.getClaim("user_id");

        if (userId == null) {
            return null;
        }

        return Long.valueOf(userId.toString());
    }
}
