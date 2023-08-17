package com.globe.gastronomy.backend.service;

import com.globe.gastronomy.backend.exception.EmailTemplateNotFoundException;
import com.globe.gastronomy.backend.model.EmailTemplate;
import com.globe.gastronomy.backend.repository.EmailTemplateRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {
    private final EmailTemplateRepository emailTemplateRepository;

    public EmailTemplateService(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    public EmailTemplate getEmailTemplateByEmailTemplateNameAndLanguage(String emailTemplateName, String emailTemplateLanguage) throws EmailTemplateNotFoundException {
        return emailTemplateRepository.findEmailTemplateByTemplateNameAndTemplateLanguage(emailTemplateName, emailTemplateLanguage).orElseThrow(() -> new EmailTemplateNotFoundException("TemplateNotFound"));
    }
}
