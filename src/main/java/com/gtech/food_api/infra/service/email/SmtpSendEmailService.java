package com.gtech.food_api.infra.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.gtech.food_api.core.email.EmailProperties;
import com.gtech.food_api.domain.service.email.SendEmailService;
import com.gtech.food_api.infra.service.email.exceptions.EmailException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class SmtpSendEmailService implements SendEmailService {

    // java mail sender é o sender de email padrão do spring boot
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailProperties emailProperties;

    @Autowired
    private Configuration freemarker;
    
    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = createMimeMessage(message);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new EmailException("Failed to send email", e);
        }
    }

    /**
     * Cria uma mensagem MIME com o remetente, destinatários, assunto e corpo HTML.
     * 
     * O que faz:
     * - Processa o template de email substituindo as variáveis e gerando o HTML final
     * - Cria uma mensagem MIME com o remetente, destinatários, assunto e corpo HTML
     * - Envia o email através do servidor SMTP configurado (Amazon SES, SendGrid, etc.)
     */
    protected MimeMessage createMimeMessage(Message message)throws MessagingException {
        String body = processTemplate(message);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setFrom(emailProperties.getSender());
        helper.setTo(message.getRecipients().toArray(new String[0]));
        helper.setSubject(message.getSubject());
        helper.setText(body, true); // true para indicar que o corpo é HTML

        return mimeMessage;
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
