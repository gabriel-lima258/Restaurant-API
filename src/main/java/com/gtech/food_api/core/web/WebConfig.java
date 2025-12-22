package com.gtech.food_api.core.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing) para permitir
 * requisições do frontend.
 * 
 * Esta configuração permite que o frontend rodando em localhost:3000
 * faça requisições para a API rodando em localhost:8080.
 * 
 * CORS é um mecanismo de segurança do navegador que bloqueia requisições
 * entre diferentes origens (protocolo, domínio ou porta). Esta classe
 * configura o Spring para permitir essas requisições cross-origin.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura os mapeamentos de CORS para todas as rotas da API.
     * 
     * Este método é chamado pelo Spring durante a inicialização da aplicação
     * para configurar quais origens podem fazer requisições para a API.
     * 
     * @param registry o registro de configuração CORS do Spring MVC
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // Configura CORS para todas as rotas da API (/**)
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("*");
    }
}

