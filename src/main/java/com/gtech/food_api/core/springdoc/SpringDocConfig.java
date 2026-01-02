package com.gtech.food_api.core.springdoc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(name = "security_auth", 
            type = SecuritySchemeType.OAUTH2, 
            flows = @OAuthFlows(authorizationCode = @OAuthFlow(
                authorizationUrl = "${springdoc.oAuth2.authorizationUrl}",
                tokenUrl = "${springdoc.oAuth2.tokenUrl}",
                scopes = {
                    @OAuthScope(name = "READ", description = "Read access to the API"),
                    @OAuthScope(name = "WRITE", description = "Write access to the API")
                }
            )))
public class SpringDocConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Delivery Food API")
                .version("v1")
                .description("API de delivery de comida")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .termsOfService("https://www.apache.org/licenses/LICENSE-2.0"))
            .externalDocs(new ExternalDocumentation()
                .description("Github")
                .url("https://github.com/gabriel-lima258/Restaurant-API"));
    }
}
