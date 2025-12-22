package com.gtech.food_api.domain.service.email;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

public interface SendEmailService {
    
    void send(Message message);

    @Builder
    @Getter
    class Message {
        // @Singular é usado para indicar que o atributo é um conjunto de valores
        // ex: @Singular("recipient") private Set<String> recipients;
        // vai gerar um método addRecipient(String recipient)
        @Singular
        private Set<String> recipients;
        @NotBlank
        private String subject;
        @NotBlank
        private String body;
    }
}
