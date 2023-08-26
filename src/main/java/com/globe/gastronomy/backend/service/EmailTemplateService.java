package com.globe.gastronomy.backend.service;

import com.globe.gastronomy.backend.exception.EmailTemplateNotFoundException;
import com.globe.gastronomy.backend.model.RawEmailTemplate;
import com.globe.gastronomy.backend.repository.EmailTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailTemplateService {
    private final EmailTemplateRepository emailTemplateRepository;

    public EmailTemplateService(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    public RawEmailTemplate getEmailTemplateByEmailTemplateNameAndLanguage(String emailTemplateName, String emailTemplateLanguage) throws EmailTemplateNotFoundException {
        return emailTemplateRepository.findEmailTemplateByTemplateNameAndTemplateLanguage(emailTemplateName, emailTemplateLanguage).orElseThrow(() -> new EmailTemplateNotFoundException("TemplateNotFound"));
    }

    public List<RawEmailTemplate> getAllTemplates(){
        return emailTemplateRepository.findAll();
    }
}
