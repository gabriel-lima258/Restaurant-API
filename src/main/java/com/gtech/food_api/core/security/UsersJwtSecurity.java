package com.gtech.food_api.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.gtech.food_api.domain.repository.OrderRepository;
import com.gtech.food_api.domain.repository.RestaurantRepository;

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
    
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

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
    public Long getUserId() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();
        Object userId = jwt.getClaim("user_id");

        if (userId == null) {
            return null;
        }

        return Long.valueOf(userId.toString());
    }

    // verifica se o usuário autenticado é igual ao usuário passado como parâmetro
    // evita vulnerabilidades de segurança casos de clients seja credentials
    public boolean userAuthenticatedEquals(Long userId) {
		return getUserId() != null && userId != null
				&& getUserId().equals(userId);
	}

    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated();
    }

    // verifica se o usuário autenticado tem a authority passada como parâmetro
    public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}

    public boolean hasReadScope() {
        return hasAuthority("SCOPE_READ");
    }

    public boolean hasWriteScope() {
        return hasAuthority("SCOPE_WRITE");
    }

    // ---------------- KITCHENS ----------------

    public boolean canViewKitchens() {
        return hasReadScope() && isAuthenticated();
    }

    public boolean canEditKitchens() {
        return hasWriteScope() && hasAuthority("EDITAR_COZINHAS");
    }

    // ---------------- RESTAURANTS ----------------

    public boolean canViewRestaurants() {
        return hasReadScope() && isAuthenticated();
    }

    public boolean canEditRestaurants() {
        return hasWriteScope() && hasAuthority("EDITAR_RESTAURANTES");
    }

    public boolean canManagerOwnerRestaurant(Long restaurantId) {
        return hasWriteScope() && hasAuthority("EDITAR_RESTAURANTES") || managerRestaurant(restaurantId);
    }

    // verifica se o usuário autenticado é responsável por um restaurante
    public boolean managerRestaurant(Long restaurantId) {
        if (restaurantId == null) {
            return false;
        }
        return restaurantRepository.existsResponsible(restaurantId, getUserId());
    }

    // ---------------- ORDERS ----------------

    public boolean canViewOrders() {
        return hasReadScope() && isAuthenticated();
    }

    public boolean canViewOrdersList(Long clientId, Long restaurantId) {
        return hasReadScope() && isAuthenticated() && 
        hasAuthority("CONSULTAR_PEDIDOS") || 
        userAuthenticatedEquals(clientId) || managerRestaurant(restaurantId);
    }

    public boolean canAddOrders() {
        return hasWriteScope() && isAuthenticated();
    }

    // verifica se o usuário autenticado é responsável por um pedido
    public boolean isOrderManagedBy(String orderCode) {
        return orderRepository.isOrderManagedBy(orderCode, getUserId());
    }

    // verifica se o usuário autenticado pode gerenciar um pedido usado no HAL
    public boolean canManagerOrder(String orderCode) {
        return hasWriteScope() && hasAuthority("GERENCIAR_PEDIDOS") || isOrderManagedBy(orderCode);
    }

    // ---------------- PAYMENTS ----------------

    public boolean canViewPayments() {
        return hasReadScope() && isAuthenticated();
    }

    public boolean canEditPayments() {
        return hasWriteScope() && hasAuthority("EDITAR_FORMAS_PAGAMENTO");
    }

    // ---------------- CITIES ----------------

    public boolean canViewCities() {
        return hasReadScope() && isAuthenticated();
    }

    public boolean canEditCities() {
        return hasWriteScope() && hasAuthority("EDITAR_CIDADES");
    }

    // ---------------- STATES ----------------

    public boolean canViewStates() {
        return hasReadScope() && isAuthenticated();
    }

    public boolean canEditStates() {
        return hasWriteScope() && hasAuthority("EDITAR_ESTADOS");
    }

    // ---------------- USERS GROUPS PERMISSIONS ----------------

    public boolean canViewUsersGroupsPermissions() {
        return hasReadScope() && isAuthenticated() && hasAuthority("CONSULTAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    public boolean canEditUsersGroupsPermissions() {
        return hasWriteScope() && isAuthenticated() && hasAuthority("EDITAR_USUARIOS_GRUPOS_PERMISSOES");
    }

    // ---------------- REPORTS ----------------

    public boolean canGenerateReports() {
        return hasReadScope() && isAuthenticated() && hasAuthority("GERAR_RELATORIOS");
    }

}
