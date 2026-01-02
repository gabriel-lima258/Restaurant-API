package com.gtech.food_api.core.springdoc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.gtech.food_api.api.V2.controller.exceptions.ExceptionsDTO;

import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.core.converter.ModelConverters;
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

    private static final String badRequestResponse = "BadRequestResponse";
    private static final String notFoundResponse = "NotFoundResponse";
    private static final String notAcceptableResponse = "NotAcceptableResponse";
    private static final String internalServerErrorResponse = "InternalServerErrorResponse";
    
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
            ))
            .components(new Components()
                .schemas(generateSchemas())
                .responses(generateResponses())
            );
    }

    // customização global de http methods da API para adicionar respostas padrão para erros
    @Bean
    public OpenApiCustomizer customizerOpenApi() {
        return openApi -> {
            openApi.getPaths()
                .values()
                .forEach(pathItem -> pathItem.readOperationsMap()
                    .forEach((httpMethod, operation) -> {
                        ApiResponses responses = operation.getResponses();
                        switch (httpMethod) {
                            case GET:
                                responses.addApiResponse("406", new ApiResponse().$ref("NotAcceptableResponse"));
                                responses.addApiResponse("500", new ApiResponse().$ref("InternalServerErrorResponse"));
                                break;
                            case POST:
                                responses.addApiResponse("400", new ApiResponse().$ref("BadRequestResponse"));
                                responses.addApiResponse("500", new ApiResponse().description("Internal Server Error"));
                                break;
                            case PUT:
                                responses.addApiResponse("400", new ApiResponse().$ref("BadRequestResponse"));
                                responses.addApiResponse("500", new ApiResponse().$ref("InternalServerErrorResponse"));
                                break;
                            case DELETE:
                                responses.addApiResponse("500", new ApiResponse().$ref("InternalServerErrorResponse"));
                                break;
                            default:
                                responses.addApiResponse("500", new ApiResponse().$ref("InternalServerErrorResponse"));
                                break;
                        }
                    }));
        };
    }

    // geração de schemas para as exceções e objetos
    private Map<String, Schema> generateSchemas() {
        final Map<String, Schema> schemas = new HashMap<>();

        Map<String, Schema> exceptionsSchema = ModelConverters.getInstance().read(ExceptionsDTO.class); // schema para a classe ExceptionsDTO
        Map<String, Schema> fieldSchema = ModelConverters.getInstance().read(ExceptionsDTO.Field.class); // schema para a classe Field

        schemas.putAll(exceptionsSchema); // adiciona o schema para a classe ExceptionsDTO
        schemas.putAll(fieldSchema); // adiciona o schema para a classe Field

        return schemas;
    }

    // geração de responses para as exceções e objetos isso serve para que o swagger possa exibir as exceções e objetos de forma consistente
    private Map<String, ApiResponse> generateResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
                .addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<ExceptionsDTO>().$ref("Exceptions")));

        apiResponseMap.put(badRequestResponse, new ApiResponse()
                .description("Bad Request")
                .content(content));

        apiResponseMap.put(notFoundResponse, new ApiResponse()
                .description("Resource Not Found")
                .content(content));

        apiResponseMap.put(notAcceptableResponse, new ApiResponse()
                .description("Not Acceptable Resource")
                .content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
                .description("Internal Server Error")
                .content(content));

        return apiResponseMap;
    }
}
