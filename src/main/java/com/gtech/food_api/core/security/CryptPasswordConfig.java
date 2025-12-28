package com.gtech.food_api.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class CryptPasswordConfig {
    
    @Bean   
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
