package com.gtech.food_api.core.jackson;

import org.springframework.data.domain.Page;
import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * Serializer customizado para objetos Page do Spring Data.
 * 
 * Esta classe customiza a serialização JSON de objetos Page retornados pelos controllers,
 * garantindo um formato consistente e padronizado para respostas paginadas.
 * 
 * Como funciona:
 * - A anotação @JsonComponent faz com que o Spring Boot registre automaticamente este serializer
 * - Quando um objeto Page é serializado para JSON, este serializer é usado automaticamente
 * - O formato de saída inclui: content, size, totalElements, totalPages e number
 * 
 * Formato de saída JSON:
 * {
 *   "content": [...],           // Array com os elementos da página
 *   "size": 10,                  // Tamanho da página (quantidade de elementos por página)
 *   "totalElements": 50,         // Total de elementos em todas as páginas
 *   "totalPages": 5,             // Total de páginas disponíveis
 *   "number": 0                  // Número da página atual (começa em 0)
 * }
 * 
 * Exemplo de resposta JSON gerada:
 * 
 * GET /orders?page=0&size=10
 * 
 * {
 *   "content": [
 *     {
 *       "code": "550e8400-e29b-41d4-a716-446655440000",
 *       "subtotal": 50.00,
 *       "feeShipping": 10.00,
 *       "totalValue": 60.00,
 *       "status": "CREATED",
 *       "createdAt": "2025-01-15T10:30:00Z"
 *     },
 *     ...
 *   ],
 *   "size": 10,
 *   "totalElements": 50,
 *   "totalPages": 5,
 *   "number": 0
 * }
 * 
 * Benefícios:
 * - Formato consistente em toda a aplicação
 * - Facilita a implementação de paginação no frontend
 * - Remove informações desnecessárias do objeto Page padrão do Spring Data
 * - Customização centralizada do formato de resposta
 */
@JsonComponent
public class PageJacksonSerializer extends JsonSerializer<Page<?>> {
    
    @Override
    public void serialize(Page<?> page, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject(); 
        gen.writeObjectField("content", page.getContent()); 
        gen.writeNumberField("size", page.getSize()); 
        gen.writeNumberField("totalElements", page.getTotalElements()); 
        gen.writeNumberField("totalPages", page.getTotalPages()); 
        gen.writeNumberField("number", page.getNumber()); 
        gen.writeEndObject();
    }
}   