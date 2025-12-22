package com.gtech.food_api.infra.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.gtech.food_api.core.email.EmailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class SandboxSendEmailService extends SmtpSendEmailService {

    @Autowired
    private EmailProperties emailProperties;

    /*
     * Sobrescreve o método createMimeMessage da classe pai (SmtpSendEmailService)
     * para adicionar o destinatário do email de sandbox.
     * 
     * O que faz:
     * - Cria uma mensagem MIME com o remetente, destinatários, assunto e corpo HTML
     * - Modifica o assunto para SANDBOX - <assunto do email>
     * - Modifica o destinatário do email de sandbox
     * - Retorna a mensagem MIME
     */
    @Override
    protected MimeMessage createMimeMessage(Message message) throws MessagingException {
        MimeMessage mimeMessage = super.createMimeMessage(message);
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setSubject("SANDBOX - " + message.getSubject());
        helper.setTo(emailProperties.getSandbox().getDestination());

        return mimeMessage;
    }

}
