package com.gtech.food_api.core.security;

import java.util.Collections;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing) para a aplicação.
 * 
 * CORS é um mecanismo de segurança implementado pelos navegadores que permite que uma aplicação
 * web em um domínio acesse recursos de outro domínio. Sem essa configuração, requisições de
 * origens diferentes seriam bloqueadas pelo navegador por questões de segurança.
 * 
 * Esta classe configura um filtro CORS global que será aplicado a todas as requisições HTTP
 * recebidas pela aplicação. O filtro é registrado com a maior precedência possível para garantir
 * que seja executado antes de outros filtros de segurança.
 * 
 * Configurações aplicadas:
 * - allowCredentials: false - Não permite envio de credenciais (cookies, headers de autenticação)
 *   nas requisições cross-origin. Se precisar enviar credenciais, deve ser true e os allowedOrigins
 *   não podem ser "*", devem ser específicos.
 * 
 * - allowedOrigins: "*" - Permite requisições de qualquer origem. Em produção, é recomendado
 *   especificar apenas as origens permitidas por questões de segurança.
 * 
 * - allowedMethods: "*" - Permite todos os métodos HTTP (GET, POST, PUT, DELETE, PATCH, OPTIONS, etc.)
 * 
 * - allowedHeaders: "*" - Permite todos os headers HTTP nas requisições cross-origin
 * 
 * IMPORTANTE: Esta configuração permite acesso de qualquer origem, o que é útil para desenvolvimento
 * mas deve ser restringida em produção para apenas as origens confiáveis.
 */
@Configuration
public class CorsConfig {

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(false);
		config.setAllowedOrigins(Collections.singletonList("*"));
		config.setAllowedMethods(Collections.singletonList("*"));
		config.setAllowedHeaders(Collections.singletonList("*"));

		// habilitar o CORS para todas as rotas da aplicação
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);

		return bean;
	}

}