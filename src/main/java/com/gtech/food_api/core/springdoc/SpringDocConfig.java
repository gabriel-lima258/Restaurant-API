package com.gtech.food_api.core.springdoc;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
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
                .url("https://github.com/gabriel-lima258/Restaurant-API"))
            .tags(Arrays.asList(
                new Tag().name("Cities").description("Operations for managing cities"),
                new Tag().name("States").description("Operations for managing states"),
                new Tag().name("Kitchens").description("Operations for managing kitchen types"),
                new Tag().name("Groups").description("Operations for managing user groups"),
                new Tag().name("Permissions").description("Operations for viewing permissions"),
                new Tag().name("Payment Methods").description("Operations for managing payment methods"),
                new Tag().name("Users").description("Operations for managing users"),
                new Tag().name("Restaurants").description("Operations for managing restaurants"),
                new Tag().name("Products").description("Operations for managing products"),
                new Tag().name("Orders").description("Operations for managing orders"),
                new Tag().name("User Groups").description("Operations for managing user-group associations"),
                new Tag().name("Group Permissions").description("Operations for managing group-permission associations"),
                new Tag().name("Restaurant Payment Methods").description("Operations for managing restaurant-payment method associations"),
                new Tag().name("Restaurant Responsibles").description("Operations for managing restaurant responsibles"),
                new Tag().name("Status Orders").description("Operations for managing order status"),
                new Tag().name("Reports").description("Operations for generating reports"),
                new Tag().name("Product Photos").description("Operations for managing product photos")
            ));
    }
}
