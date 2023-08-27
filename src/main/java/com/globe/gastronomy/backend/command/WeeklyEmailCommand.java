package com.globe.gastronomy.backend.command;

import com.globe.gastronomy.backend.constants.EmailConstants;
import com.globe.gastronomy.backend.email.EmailTemplate;
import com.globe.gastronomy.backend.email.WeeklyEmailTemplate;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.model.User;
import com.globe.gastronomy.backend.service.EmailTemplateService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeeklyEmailCommand extends EmailCommand {
    private final EmailTemplateService emailTemplateService;

    public WeeklyEmailCommand(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    EmailTemplate getTemplate(String language) {
        return new WeeklyEmailTemplate(emailTemplateService);
    }

    @Override
    Map<String, String> getTokens(User user) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put(EmailConstants.FIRSTNAME.getStr(), user.getFirstName());
        tokens.put(EmailConstants.LASTNAME.getStr(), user.getLastName());
        return tokens;
    }

    @Override
    EmailRequest generateRequest(User user, RawEmailTemplate emailTemplate) {
        return new WeeklyEmailRequest("mahmutcelik1618@gmail.com", user.getEmail(), EmailConstants.WEEKLY_EMAIL_TEMPLATE.getStr(), emailTemplate.getTemplateContent()); // TODO: 8/23/2023 FROM PART WILL CHANGE

    }
}
