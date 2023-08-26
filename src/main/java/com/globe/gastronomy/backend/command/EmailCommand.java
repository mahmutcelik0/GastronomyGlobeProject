package com.globe.gastronomy.backend.command;

import com.globe.gastronomy.backend.email.EmailTemplate;
import com.globe.gastronomy.backend.email.EmailTemplateFactory;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.utils.LogUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

public abstract class EmailCommand {
    @Autowired
    private JavaMailSender mailSender;


    public EmailResponse sendEmail(User user, String language) {
        RawEmailTemplate emailTemplate = EmailTemplateFactory.generateTemplate(getTemplate(language), getTokens(user), language);
        EmailRequest emailRequest = generateRequest(user, emailTemplate);
        try {
            mailSender.send(generateMailMessage(emailRequest));
            return new EmailResponse("Mail has sent successfully!");
        } catch (MailException e) {
            LogUtil.printLog("Problem in EmailCommand", EmailCommand.class);
            return new EmailResponse(e.getMessage());
        }
    }

    protected MimeMessage generateMailMessage(EmailRequest request) {
        MimeMessage message = mailSender.createMimeMessage();

        setEmailMessage(message, request);

        return message;
    }

    abstract EmailTemplate getTemplate(String language);

    abstract Map<String, String> getTokens(User user);

    abstract EmailRequest generateRequest(User user, RawEmailTemplate emailTemplate);

    abstract void setEmailMessage(MimeMessage message, EmailRequest request);
}
