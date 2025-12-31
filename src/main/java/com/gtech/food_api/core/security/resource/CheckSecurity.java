package com.gtech.food_api.core.security.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Container de anotações customizadas para verificação de permissões de acesso.
 * 
 * UTILIDADE PRINCIPAL:
 * Esta classe serve como um "namespace" para organizar anotações de segurança
 * agrupadas por recurso (Kitchens, Orders, Restaurants, etc.), evitando
 * repetição de código e centralizando as regras de autorização.
 * 
 * POR QUE USAR ANOTAÇÕES CUSTOMIZADAS?
 * 
 * ❌ SEM anotações customizadas (código repetitivo):
 * 
 * @PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
 * @PutMapping("/{id}")
 * public Kitchen update(...) { }
 * 
 * @PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
 * @DeleteMapping("/{id}")
 * public void delete(...) { }
 * 
 * ✅ COM anotações customizadas (código limpo):
 * 
 * @CheckSecurity.Kitchens.EditPermission
 * @PutMapping("/{id}")
 * public Kitchen update(...) { }
 * 
 * COMO FUNCIONA INTERNAMENTE:
 * 
 * 1. @CheckSecurity.Kitchens.EditPermission é aplicada no método
 * 2. Spring Security detecta @PreAuthorize dentro da anotação customizada
 * 3. Avalia a expressão: hasAuthority('EDITAR_COZINHAS')
 * 4. Busca 'EDITAR_COZINHAS' nas authorities do usuário (extraídas do JWT)
 * 5. Se encontrada, permite acesso; caso contrário, lança AccessDeniedException (403)
 * 
 * FLUXO COMPLETO DE AUTORIZAÇÃO:
 * 
 * Requisição → Resource Server valida JWT → jwtAuthenticationConverter extrai authorities
 * → SecurityContext armazena authorities → @CheckSecurity.Kitchens.EditPermission verifica
 * → @PreAuthorize("hasAuthority('EDITAR_COZINHAS')") valida → Acesso permitido/negado
 */
public @interface CheckSecurity {
    
    public @interface Kitchens {
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
    }

    public @interface Restaurants {
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_RESTAURANTES')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanAdminManager {
        }

        // @usersJwtSecurity acessa o bean dessa classe utilizando o metodo, #id é o parametro existente no endpoint, é o dono do restaurant or ?
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and "
                    + "hasAuthority('EDITAR_RESTAURANTES') or "
                    + "@usersJwtSecurity.managerRestaurant(#restaurantId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanOnwerManager {
        }

        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
    }

    /**
     * REGRAS DE NEGÓCIO:
     * Um pedido pode ser consultado apenas por:
     * 1. Administradores com permissão 'CONSULTAR_PEDIDOS'
     * 2. O cliente que fez o pedido
     * 3. O responsável/gerente do restaurante onde o pedido foi feito
     */
    public @interface Orders {
        
        /**
         * Anotação para verificar permissão de visualização de pedidos.
         * 1️⃣ @PreAuthorize (ANTES da execução do método):
         * 2️⃣ @PostAuthorize (DEPOIS da execução do método):
         *    - Executa o método e obtém o resultado (returnObject) DTO
         * 
         * POR QUE USAR @PostAuthorize AQUI?
         * Porque precisamos acessar dados do pedido retornado (cliente e restaurante)
         * para decidir se o usuário pode ver esse pedido específico.
         * 
         * ESTRUTURA DO returnObject:
         * - Método retorna: ResponseEntity<OrderDTO>
         * - returnObject = ResponseEntity (wrapper)
         * - returnObject.body = OrderDTO (dados reais do pedido)
         * - returnObject.body.client = UserDTO (cliente que fez o pedido)
         * - returnObject.body.restaurant = RestaurantDTO (restaurante do pedido)
         * 
         * CONDIÇÕES DE AUTORIZAÇÃO (qualquer uma permite acesso):
         * 
         * ✅ Condição 1: hasAuthority('CONSULTAR_PEDIDOS')
         *    → Usuário admin com permissão de consultar qualquer pedido
         * 
         * ✅ Condição 2: @usersJwtSecurity.getUserId() == returnObject.body.client.id
         *    → Usuário logado é o cliente que fez o pedido
         * 
         * ✅ Condição 3: @usersJwtSecurity.managerRestaurant(returnObject.body.restaurant.id)
         *    → Usuário logado é responsável/gerente do restaurante
         */
        @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "
                    + "@usersJwtSecurity.getUserId() == returnObject.body.client.id or "
                    + "@usersJwtSecurity.managerRestaurant(returnObject.body.restaurant.id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
    }

}
