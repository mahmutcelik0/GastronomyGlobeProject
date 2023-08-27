package com.globe.gastronomy.backend.email;

import com.globe.gastronomy.backend.constants.EmailConstants;
import com.globe.gastronomy.backend.exception.EmailTemplateNotFoundException;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.service.EmailTemplateService;
import org.springframework.stereotype.Service;

@Service
public class WeeklyEmailTemplate extends EmailTemplate{
    private final EmailTemplateService emailTemplateService;

    public WeeklyEmailTemplate(EmailTemplateService emailTemplateService) {
        this.emailTemplateService = emailTemplateService;
    }

    @Override
    RawEmailTemplate getTemplate(String language) {
        try {
            return emailTemplateService.getEmailTemplateByEmailTemplateNameAndLanguage(EmailConstants.WEEKLY_EMAIL_TEMPLATE.getStr(), language);
        } catch (EmailTemplateNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    EmailTemplateBuilder setBuilder() {
        return new WeeklyEmailTemplateBuilder();
    }
}
