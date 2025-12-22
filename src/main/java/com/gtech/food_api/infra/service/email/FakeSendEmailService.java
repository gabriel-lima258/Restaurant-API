package com.gtech.food_api.infra.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.gtech.food_api.core.email.EmailProperties;
import com.gtech.food_api.domain.service.email.SendEmailService;
import com.gtech.food_api.infra.service.email.exceptions.EmailException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeSendEmailService implements SendEmailService {

    @Autowired
    private Configuration freemarker;

    @Override
    public void send(Message message) {
        try {
            String body = processTemplate(message);
            log.info("Simulando envio FAKE de email para: {} com o corpo: {}", message.getRecipients(), body);
        } catch (Exception e) {
            throw new EmailException("Failed to send email", e);
        }
    }  
    
    private String processTemplate(Message message) {
        try {
            Template template = freemarker.getTemplate(message.getBody());
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
        } catch (Exception e) {
            throw new EmailException("Failed to process email template", e);
        }
    }
}
