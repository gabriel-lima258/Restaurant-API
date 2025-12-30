package com.gtech.food_api.core.security.authorization;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Classe de propriedades de configuração para o KeyStore JWT.
@Getter
@Setter
@Validated
@Component
@ConfigurationProperties("algafood.jwt.keystore")
public class JwtKeyStoreProperties {
    
    // Localização do arquivo KeyStore (.jks).
    @NotNull
	private Resource jksLocation;
	
	// Senha para abrir o arquivo KeyStore (.jks)
	@NotBlank
	private String password;
	
	// Nome do par de chaves no KeyStore.
	@NotBlank
	private String keypairAlias;
}
