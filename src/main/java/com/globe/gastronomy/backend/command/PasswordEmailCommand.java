package com.globe.gastronomy.backend.command;

import com.globe.gastronomy.backend.constants.EmailConstants;
import com.globe.gastronomy.backend.email.EmailTemplate;
import com.globe.gastronomy.backend.email.PasswordEmailTemplate;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.service.EmailTemplateService;
import com.globe.gastronomy.backend.utils.LogUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PasswordEmailCommand extends EmailCommand {
    private final EmailTemplateService emailTemplateService;

    public PasswordEmailCommand(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    EmailTemplate getTemplate(String language) {
        return new PasswordEmailTemplate(emailTemplateService);
    }

    @Override
    Map<String, String> getTokens(User user) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(EmailConstants.FIRSTNAME.getStr(), user.getFirstName());
        tokens.put(EmailConstants.LASTNAME.getStr(), user.getLastName());
        tokens.put(EmailConstants.PASSWORD_REFRESH_LINK.getStr(), "https://www.google.com"); // TODO: 8/23/2023 IT WILL CHANGE
        return tokens;
    }

    @Override
    EmailRequest generateRequest(User user, RawEmailTemplate emailTemplate) {
        return new PasswordEmailRequest("mahmutcelik1618@gmail.com", user.getEmail(), EmailConstants.FORGOT_PASSWORD_SUBJECT.getStr(), emailTemplate.getTemplateContent()); // TODO: 8/23/2023 FROM PART WILL CHANGE
    }

    @Override
    void setEmailMessage(MimeMessage message, EmailRequest request) {
        try {
            message.setFrom(request.getFrom());
            message.setRecipients(MimeMessage.RecipientType.TO, request.getRecipients());
            message.setSubject(request.getSubject());
            message.setContent(request.getContent(), "text/html; charset=utf-8");
        } catch (MessagingException e) {
            LogUtil.printLog("MESSAGE PROBLEM :", PasswordEmailCommand.class);
        }
    }
}
