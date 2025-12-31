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
        @PreAuthorize("@usersJwtSecurity.canEditKitchens()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
        @PreAuthorize("@usersJwtSecurity.canViewKitchens()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
    }

    public @interface Restaurants {
        @PreAuthorize("@usersJwtSecurity.canEditRestaurants()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanAdminManager {
        }

        // @usersJwtSecurity acessa o bean dessa classe utilizando o metodo, #id é o parametro existente no endpoint, é o dono do restaurant or ?
        @PreAuthorize("@usersJwtSecurity.canManagerOwnerRestaurant(#restaurantId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanOnwerManager {
        }

        @PreAuthorize("@usersJwtSecurity.canViewRestaurants()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
    }

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
         */
        @PreAuthorize("@usersJwtSecurity.canViewOrders()")
        @PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') or "
                    + "@usersJwtSecurity.userAuthenticatedEquals(returnObject.body.client.id) or "
                    + "@usersJwtSecurity.managerRestaurant(returnObject.body.restaurant.id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }

        @PreAuthorize("@usersJwtSecurity.canViewOrdersList(#filter.clientId, #filter.restaurantId)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanViewList {
        }

        @PreAuthorize("@usersJwtSecurity.canAddOrders()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanAddOrders {
        }

        @PreAuthorize("@usersJwtSecurity.canManagerOrder(#orderCode)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanManageOrder {
        }
    }

    public @interface Payments {
        @PreAuthorize("@usersJwtSecurity.canViewPayments()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
        @PreAuthorize("@usersJwtSecurity.canEditPayments()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface Cities {
        @PreAuthorize("@usersJwtSecurity.canViewCities()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
        @PreAuthorize("@usersJwtSecurity.canEditCities()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface States {
        @PreAuthorize("@usersJwtSecurity.canViewStates()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
        @PreAuthorize("@usersJwtSecurity.canEditStates()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }
    }

    public @interface UsersGroupsPermissions {
        @PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated() and "
                    + "@usersJwtSecurity.userAuthenticatedEquals(#id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanUpdatePassword {
        }

        @PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated() and "
                    + "hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or "
                    + "@usersJwtSecurity.userAuthenticatedEquals(#id)")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanUpdateUser {
        }

        @PreAuthorize("@usersJwtSecurity.canEditUsersGroupsPermissions()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanEdit {
        }

        @PreAuthorize("@usersJwtSecurity.canViewUsersGroupsPermissions()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanView {
        }
    }

    public @interface Reports {
        @PreAuthorize("@usersJwtSecurity.canGenerateReports()")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanGenerateReports {
        }
    }

}
