package com.gtech.food_api.core.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.Filter;
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

    /*
	 * ShallowEtagHeaderFilter é um filtro que adiciona o header ETag na resposta HTTP.
	 * O ETag é um hash do conteúdo da resposta.
	 * Se o cliente fizer uma nova requisição e o conteúdo for o mesmo, o cliente receberá o conteúdo da cache.
	 * Toda vez que enviar uma requisição, sera feita um If-None-Match com o ETag do conteúdo.
	 * Se o ETag for o mesmo, o cliente receberá o conteúdo do cache mesmo se o tempo de expiração do cache for atingido. com HttpStatus 304 Not Modified.
	 * Se o ETag for diferente, o cliente receberá o novo conteúdo. Retornando HttpStatus 200 OK.
	 */
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}

	// Adiciona o view controller para a página de login criada no templates/pages/login.html
	// O view controller é um componente que adiciona uma view ao contexto do Spring MVC.
	// Criado com Thymeleaf.
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("pages/login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}
}

