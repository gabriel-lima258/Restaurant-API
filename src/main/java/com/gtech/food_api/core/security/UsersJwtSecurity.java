package com.gtech.food_api.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UsersJwtSecurity {
    
    // retorna o authentication do usuario logado pelo token
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Long getUserId() {
        Jwt jwt = (Jwt) getAuthentication().getPrincipal();

        Object userId = jwt.getClaim("user_id");

        if (userId == null) {
            return null;
        }

        return Long.valueOf(userId.toString());
    }
}
