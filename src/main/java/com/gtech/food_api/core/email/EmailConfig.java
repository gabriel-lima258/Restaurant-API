package com.gtech.food_api.core.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gtech.food_api.domain.service.email.SendEmailService;
import com.gtech.food_api.infra.service.email.FakeSendEmailService;
import com.gtech.food_api.infra.service.email.SandboxSendEmailService;
import com.gtech.food_api.infra.service.email.SmtpSendEmailService;

@Configuration
public class EmailConfig {
    
    @Autowired
    private EmailProperties emailProperties;

    /**
     * Cria e configura o serviço de envio de email.
     * 
     * O que faz:
     * - Se o tipo de email for FAKE, cria e configura o serviço de envio de email FAKE
     * - Se o tipo de email for SMTP, cria e configura o serviço de envio de email SMTP
     */
    @Bean
    public SendEmailService sendEmailService() {
        switch (emailProperties.getType()) {
            case FAKE:
                return new FakeSendEmailService();
            case SMTP:
                return new SmtpSendEmailService();
            case SANDBOX:
                return new SandboxSendEmailService();
            default:
                return null;
        }
    }
}
