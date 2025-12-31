package com.gtech.food_api.core.security.resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
        @PreAuthorize("hasAuthority('EDITAR_COZINHAS') and hasAuthority('SCOPE_WRITE')")
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
        @PreAuthorize("hasAuthority('EDITAR_RESTAURANTES') and hasAuthority('SCOPE_WRITE')")
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        public @interface CanAdminManager {
        }

        // @usersJwtSecurity acessa o bean dessa classe utilizando o metodo, #id é o parametro existente no endpoint
        @PreAuthorize("hasAuthority('EDITAR_RESTAURANTES') and "
                    + "hasAuthority('SCOPE_WRITE') or "
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

}
