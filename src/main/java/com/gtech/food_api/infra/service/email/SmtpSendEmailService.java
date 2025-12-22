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
import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpSendEmailService implements SendEmailService {

    // java mail sender é o sender de email padrão do spring boot
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private Configuration freemarker;

    /**
     * Envia um email usando SMTP através do JavaMailSender do Spring Boot.
     * 
     * Este método processa o template de email, configura a mensagem MIME com
     * remetente, destinatários, assunto e corpo HTML, e envia o email através
     * do servidor SMTP configurado (Amazon SES, SendGrid, etc.).
     * 
     * @param message Objeto Message contendo os destinatários, assunto, template
     *                e variáveis para processamento do template
     * @throws EmailException se houver falha ao processar o template ou enviar o email
     */
    @Override
    public void send(Message message) {
        try {
            // Processa o template de email substituindo as variáveis e gerando o HTML final
            String body = processTemplate(message);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getSender());
            helper.setTo(message.getRecipients().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(body, true); // true para indicar que o corpo é HTML

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailException("Failed to send email", e);
        }
    }

    /**
     * Processa o template de email usando FreeMarker.
     * 
     * Este método recebe uma mensagem contendo o nome do template e as variáveis,
     * carrega o template do FreeMarker e processa substituindo as variáveis pelos
     * valores fornecidos, retornando o HTML final do email.
     * 
     * @param message Objeto Message contendo o nome do template (body) e as variáveis
     *                a serem substituídas no template
     * @return String com o HTML processado do template de email
     * @throws EmailException se houver falha ao carregar ou processar o template
     */
    private String processTemplate(Message message) {
        try {
            // Carrega o template do FreeMarker usando o nome do template armazenado no body da mensagem
            Template template = freemarker.getTemplate(message.getBody());
            // Processa o template substituindo as variáveis pelos valores fornecidos e retorna o HTML final
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, message.getVariables());
        } catch (Exception e) {
            // Lança exceção customizada em caso de erro no processamento do template
            throw new EmailException("Failed to process email template", e);
        }
    }
}
