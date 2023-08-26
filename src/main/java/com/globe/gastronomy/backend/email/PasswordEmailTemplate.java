package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.constants.EmailConstants;
import com.globe.gastronomy.backend.exception.EmailTemplateNotFoundException;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.service.EmailTemplateService;
import org.springframework.stereotype.Service;

@Service
public class PasswordEmailTemplate extends EmailTemplate {

    private final EmailTemplateService emailTemplateService;

    public PasswordEmailTemplate(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    RawEmailTemplate getTemplate(String language) {
        try {
            return emailTemplateService.getEmailTemplateByEmailTemplateNameAndLanguage(EmailConstants.FORGOT_PASSWORD_TEMPLATE.getStr(), language);
        } catch (EmailTemplateNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    EmailTemplateBuilder setBuilder() {
        return new PasswordEmailTemplateBuilder();
    }
}
