package com.gtech.food_api.infra.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.gtech.food_api.core.email.EmailProperties;
import com.gtech.food_api.domain.service.email.SendEmailService;
import com.gtech.food_api.infra.service.email.exceptions.EmailException;

import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpSendEmailService implements SendEmailService {

    // java mail sender é o sender de email padrão do spring boot
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailProperties emailProperties;

    @Override
    public void send(Message message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setFrom(emailProperties.getSender());
            helper.setTo(message.getRecipients().toArray(new String[0]));
            helper.setSubject(message.getSubject());
            helper.setText(message.getBody(), true); // true para indicar que o corpo é HTML

            mailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailException("Failed to send email", e);
        }
    }
}
